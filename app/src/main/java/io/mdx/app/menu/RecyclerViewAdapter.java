package io.mdx.app.menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
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
    return viewHolderLookup[viewType].createViewHolder(context, parent);
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
