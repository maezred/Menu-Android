package io.mdx.app.menu.data;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by moltendorf on 16/5/13.
 */
public class Bus<T> {
  private Subject<T, T> bus = new SerializedSubject<>(PublishSubject.<T>create());

  public void send(T o) {
    bus.onNext(o);
  }

  public Observable<T> observe() {
    return bus;
  }
}
