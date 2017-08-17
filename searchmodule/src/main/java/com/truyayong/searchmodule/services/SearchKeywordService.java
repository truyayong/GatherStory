package com.truyayong.searchmodule.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.truyayong.searchmodule.IOnSearchResultArrivedListener;
import com.truyayong.searchmodule.ISearchAidl;
import com.truyayong.searchmodule.bean.Sentence;
import com.truyayong.searchmodule.lucene.LuceneUtils;
import com.truyayong.searchmodule.utils.TimeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import rx.Observable;
import rx.Subscriber;

public class SearchKeywordService extends Service {

    private ExecutorService searchExecutor = Executors.newFixedThreadPool(2);
    private ScheduledExecutorService saveExecutor = Executors.newScheduledThreadPool(1);
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private AtomicBoolean isSearchRunning = new AtomicBoolean(false);
    private BmobDate updateDate;
    private  IOnSearchResultArrivedListener mListener;
    private RemoteCallbackList<IOnSearchResultArrivedListener> mListeners = new RemoteCallbackList<>();
    private Binder mBinder = new ISearchAidl.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void saveSearchLucene() throws RemoteException {
            saveLucene();
        }

        @Override
        public List<String> searchKeyword(String keyword) throws RemoteException {
            List<String> result = null;
            try {
                searchContent(keyword);
            } catch (ExecutionException e) {
                Log.e("truyayong", "SearchKeywordService exception : " + e.toString());
                e.printStackTrace();
            } catch (InterruptedException e) {
                Log.e("truyayong", "SearchKeywordService exception : " + e.toString());
                e.printStackTrace();
            }
            return result;
        }

        @Override
        public void registerListener(IOnSearchResultArrivedListener listener) throws RemoteException {
            //mListener = listener;
            mListeners.register(listener);
        }

        @Override
        public void unregisterListener(IOnSearchResultArrivedListener listener) throws RemoteException {
            //mListener = null;
            mListeners.unregister(listener);
        }

        public void saveLucene() {
            SaveLuceneRunnable callable = new SaveLuceneRunnable();
            searchExecutor.submit(callable);
            //saveExecutor.scheduleAtFixedRate(callable, 5, 29, TimeUnit.SECONDS);
        }

        public void searchContent(String keyword) throws ExecutionException, InterruptedException {
            SearchContentCallable callable = new SearchContentCallable(keyword);
            Future<List<String>> future =  searchExecutor.submit(callable);
        }
    };

    public SearchKeywordService() {
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

    class SaveLuceneRunnable implements Runnable {

        private volatile List<Sentence> mSentences = new ArrayList<>();
        private CyclicBarrier cyclicBarrier = new CyclicBarrier(2,null);

        @Override
        public void run() {
            try {
                lock.lock();

                //如果正在搜索，则线程休眠
                while (isSearchRunning.get() == true) {
                    Log.e("truyayong", "SaveLucene isSearchRunning await");
                    Thread.currentThread().yield();
                    condition.await(5, TimeUnit.SECONDS);
                }

                BmobQuery<Sentence> query = new BmobQuery<>();
                query.addWhereGreaterThan("createdAt",getUpdateDate());
                Log.e("truyayong", "SaveLucene enter thread : " + Process.myTid() + " process : " + Process.myPid());
                //升序查询，index = size - 1时为最新时间
                query.order("createAt");
                //每次查询最多10条
                query.setLimit(10);
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
                                    LuceneUtils.saveDoc(s.getObjectId(), s.getTitle(), s.getContent());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        //将最新的时间存入本地
                        String strLastTime = sentences.get(sentences.size() - 1).getCreatedAt();
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
                    Log.e("truyayong", "SaveLucene mSentences 30 await");
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
                Log.e("truyayong", "shutdown");
                isSearchRunning.compareAndSet(false, true);
                lock.lock();
                //用Lucene查询
                searchResult = LuceneUtils.searchKeyword(keyword);
                final int listenerCount = mListeners.beginBroadcast();
                for (int i = 0; i < listenerCount; i++) {
                    IOnSearchResultArrivedListener listener = mListeners.getBroadcastItem(i);
                    listener.onSearchResultArrived(searchResult);
                }
                mListeners.finishBroadcast();
                SaveLuceneRunnable callable = new SaveLuceneRunnable();
                saveExecutor.scheduleAtFixedRate(callable, 5, 29, TimeUnit.SECONDS);

            } finally {
                //查询之后原子变量置为false，并唤醒saveLucene线程
                isSearchRunning.compareAndSet(true, false);
                condition.signal();
                lock.unlock();
            }
            return searchResult;
        }
    }
}
