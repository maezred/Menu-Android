package io.mdx.app.menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * More generic implementation of RecyclerView.
 * Can be used in various ways with minimal, if any, extension required.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
  private static final String TAG = "PlacesListAdapter";

  private Context context;
  private List    dataSet;

  private Map<Class<?>, Integer> viewTypeLookup;
  private Factory[]              viewHolderLookup;

  private List<WeakReference<ViewHolder>> holderReferences = new LinkedList<>();
  private List<EventListener>             eventListeners   = new ArrayList<>();

  public RecyclerViewAdapter(Context context, Map<Class<?>, Factory> factories) {
    this(context, factories, new ArrayList(0));
  }

  public RecyclerViewAdapter(Context context, Map<Class<?>, Factory> factories, List dataSet) {
    Log.d(TAG, "PlacesListAdapter: Called.");

    this.context = context;
    this.dataSet = dataSet;

    // Create lookups.
    int size = factories.size();
    viewTypeLookup = new HashMap<>(size);
    viewHolderLookup = new Factory[size];

    int i = 0;
    for (Map.Entry<Class<?>, Factory> entry : factories.entrySet()) {
      viewTypeLookup.put(entry.getKey(), i);
      viewHolderLookup[i] = entry.getValue();

      i++;
    }
  }

  @Override
  public int getItemViewType(int position) {
    // Todo: add more helpful exception when no class to resource relationship exists.
    return viewTypeLookup.get(dataSet.get(position).getClass());
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    try {
      // Todo: better type safety guarantees using more generics so we don't crash here as often?
      ViewHolder holder = viewHolderLookup[viewType].createViewHolder(context, parent);

      for (EventListener listener : eventListeners) {
        setEventListenerBinding("add", holder, listener);
      }

      // Used to add events after the fact.
      holderReferences.add(new WeakReference<>(holder));

      return holder;
    } catch (final Exception exception) {
      throw new IllegalStateException("Could not construct view holder.", exception);
    }
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.bindTo(dataSet.get(position), position);
  }

  @Override
  public int getItemCount() {
    return dataSet.size();
  }

  public void changeDataSet(List dataSet) {
    this.dataSet = dataSet;

    notifyDataSetChanged();
  }

  public void addEventListener(EventListener listener) {
    eventListeners.add(listener);

    // Add event to existing view holders.
    Iterator<WeakReference<ViewHolder>> iterator = holderReferences.iterator();

    while (iterator.hasNext()) {
      WeakReference<ViewHolder> holderReference = iterator.next();
      ViewHolder                holder          = holderReference.get();

      // Since we're using weak references, these can go null if they're destroyed.
      if (holder == null) {
        iterator.remove();
      } else {
        setEventListenerBinding("add", holder, listener);
      }
    }
  }

  public void removeEventListener(EventListener listener) {
    eventListeners.remove(listener);

    // Add event to existing view holders.
    Iterator<WeakReference<ViewHolder>> iterator = holderReferences.iterator();

    while (iterator.hasNext()) {
      WeakReference<ViewHolder> holderReference = iterator.next();
      ViewHolder                holder          = holderReference.get();

      // Since we're using weak references, these can go null if they're destroyed.
      if (holder != null) {
        setEventListenerBinding("remove", holder, listener);
      } else {
        iterator.remove();
      }
    }
  }

  private void setEventListenerBinding(String mode, ViewHolder holder, EventListener listener) {
    Class<?> holderClass   = holder.getClass();
    Class<?> listenerClass = listener.getClass().getSuperclass();
    String   name          = listenerClass.getSimpleName();
    String   method        = mode + name;

    try {
      // This is a bad idea. Yeah. Diving right in: dynamically binding event listeners!
      holderClass.getMethod(method, listenerClass).invoke(holder, listener);
    } catch (Exception exception) {
      Log.d(TAG,
        "setEventListenerBinding: " +
          "Class " + holderClass.getSimpleName() + " can't " + mode + " " + name + ".\n" +
          "We are looking for: " + "public void " + method + "(" + name + " listener) { /* ... */ }"
      );
    }
  }

  public interface Factory<T extends ViewHolder> {
    T createViewHolder(Context context, ViewGroup parent);
  }

  public static abstract class ViewHolder extends RecyclerView.ViewHolder {
    protected Context context;

    public ViewHolder(Context context, ViewGroup viewGroup, int resource) {
      super(LayoutInflater.from(context).inflate(resource, viewGroup, false));

      this.context = context;
    }

    abstract public void bindTo(Object object, int position);
  }
}
