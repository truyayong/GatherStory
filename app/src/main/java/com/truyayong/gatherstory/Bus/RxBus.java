package com.truyayong.gatherstory.Bus;

import javax.security.auth.Subject;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by alley_qiu on 2017/3/14.
 */

public class RxBus {

    private static volatile RxBus mInstance;

    private final SerializedSubject bus;

    public RxBus() {
        bus = new SerializedSubject(PublishSubject.create());
    }

    public static RxBus getInstance() {
        RxBus rxBus = mInstance;
        if (mInstance == null) {
            synchronized (RxBus.class) {
                if (mInstance == null) {
                    rxBus = new RxBus();
                    mInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    /**
     * 发送消息
     * @param object
     */
    public void post(Object object) {
        bus.onNext(object);
    }

    /**
     * 接收消息
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObserverable(Class<T> eventType) {
        return bus.ofType(eventType);
    }
}
