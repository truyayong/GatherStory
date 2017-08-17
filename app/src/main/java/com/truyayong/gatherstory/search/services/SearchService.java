package com.truyayong.gatherstory.search.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.truyayong.gatherstory.content.bean.Sentence;
import com.truyayong.gatherstory.utils.TimeUtil;
import com.truyayong.searchmodule.lucene.LuceneUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.schedulers.CachedThreadScheduler;
import rx.schedulers.Schedulers;

public class SearchService extends Service {

    private Binder mBinder = new SearchBinder();

    private ExecutorService searchExecutor = Executors.newFixedThreadPool(1);
    private ScheduledExecutorService saveExecutor = Executors.newScheduledThreadPool(1);
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private AtomicBoolean isSearchRunning = new AtomicBoolean(false);
    private BmobDate updateDate;

    public SearchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private BmobDate getUpdateDate() throws IOException, ClassNotFoundException, ParseException {
        File file = new File(Environment.getExternalStorageDirectory(), "bmobdate.date");
        if (!file.exists()) {
            file.createNewFile();
            String startTime = "2017-03-08 00:00:00";
            BmobDate bmobDate = TimeUtil.string2BmobDate(startTime);
            saveUpdateDate(bmobDate);
            return bmobDate;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        updateDate = (BmobDate) objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
        return updateDate;
    }

    private void saveUpdateDate(BmobDate date) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory(), "bmobdate.date");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(date);
        fileOutputStream.close();
        objectOutputStream.close();
    }

    public class SearchBinder extends Binder {

        public void saveLucene() {
            SaveLuceneRunnable callable = new SaveLuceneRunnable();
            saveExecutor.scheduleAtFixedRate(callable, 5, 29, TimeUnit.SECONDS);
        }

        public List<String> searchContent(String keyword) throws ExecutionException, InterruptedException {
            SearchContentCallable callable = new SearchContentCallable(keyword);
            Future<List<String>> future =  searchExecutor.submit(callable);
            return future.get();
        }
    }

    class SaveLuceneRunnable implements Runnable {

        private volatile List<Sentence> mSentences = new ArrayList<>();
        private CyclicBarrier cyclicBarrier = new CyclicBarrier(2,null);


        @Override
        public void run() {
            try {
                lock.lock();

                //如果正在搜索，则线程休眠
                if (isSearchRunning.get() == true) {
                    Log.e("truyayong", "SaveLucene isSearchRunning await");
                    condition.await();
                }
                BmobQuery<Sentence> query = new BmobQuery<>();
                query.addWhereGreaterThan("createdAt",getUpdateDate());
                Log.e("truyayong", "SaveLucene enter" + Process.myTid());
                //升序查询，index = size - 1时为最新时间
                query.order("createAt");
                //每次查询最多10条
                query.setLimit(100);
                Observable<List<Sentence>> listObservable =  query.findObjectsObservable(Sentence.class);
                listObservable.subscribe(new Subscriber<List<Sentence>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable throwable) {
                    }
                    @Override
                    public void onNext(List<Sentence> sentences) {
                        mSentences = sentences;

                        //将查询到的文章局点存入Lucene索引
                        if (!mSentences.isEmpty()) {
                            for (Sentence s : mSentences) {
                                try {
                                    Log.e("truyayong", "SaveLucene content : " + s.getContent());
                                    LuceneUtils.saveDoc(s.getObjectId(), s.getTitle(), s.getContent());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        //将最新的时间存入本地
                        String strLastTime = sentences.get(sentences.size() - 1).getCreatedAt();
                        Log.e("truyayong", "SaveLucene lstatime : " + strLastTime);
                        try {
                            BmobDate dateLastTime = TimeUtil.string2BmobDate(strLastTime);
                            saveUpdateDate(dateLastTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                cyclicBarrier.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (BrokenBarrierException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                cyclicBarrier.await();
                if (mSentences.size() <= 1) {
                    Log.e("truyayong", "SaveLucene mSentences await");
                    //若查询数据库中没有新内容，则线程休眠10分钟
                    condition.await(600, TimeUnit.SECONDS);
                } else {
                    condition.await(30, TimeUnit.SECONDS);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    class SearchContentCallable implements Callable<List<String>> {

        private String keyword;

        public SearchContentCallable(String keyword) {
            this.keyword = keyword;
        }

        @Override
        public List<String> call() throws Exception {
            List<String> searchResult = new ArrayList<>();
            try {
                //现将原子变量写为true，表示正在查询
                isSearchRunning.compareAndSet(false, true);
                Log.e("truyayong", "SearchContent lock : " + Process.myTid());
                lock.lock();
                //用Lucene查询
                searchResult = LuceneUtils.searchKeyword(keyword);
                Log.e("truyayong", "SearchContent");

            } finally {
                //查询之后原子变量置为false，并唤醒saveLucene线程
                isSearchRunning.compareAndSet(true, false);
                condition.signalAll();
                lock.unlock();
            }
            return searchResult;
        }
    }
}
