package com.valtech.androidtoolkit.view;

import java.util.Iterator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.valtech.androidtoolkit.R;

public class Table extends TableLayout implements Iterable<View>
{
    private int columnCount;
    private int itemLayout;
    private Adapter adapter;


    public interface Adapter
    {
        int getItemCount();

        void onCreateCell(View cell, int index);
    }


    public Table(Context context) {
        super(context);
        initTable(context);
    }

    public Table(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTable(context);
        initAttributes(context, attrs);
    }

    private void initTable(Context context) {
        columnCount = 2;
        itemLayout = -1;
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Table);

        columnCount = typedArray.getInteger(R.styleable.Table_columnCount, columnCount);
        itemLayout = typedArray.getResourceId(R.styleable.Table_item_layout, itemLayout);

        typedArray.recycle();
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public int getItemLayout() {
        return itemLayout;
    }

    public void setItemLayout(int itemLayout) {
        this.itemLayout = itemLayout;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    public void notifyDataSetChanged() {
        removeAllViews();
        int itemCount = adapter.getItemCount();
        for (int i = 0; i < itemCount; ++i) {
            adapter.onCreateCell(createCell(), i);
        }
        spanTable();
    }

    protected TableRow createRow() {
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                                      android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        addView(row);
        return row;
    }

    protected View createCell() {
        // Create a new row if needed.
        TableRow row;
        if (getChildCount() == 0) {
            row = createRow();
        } else {
            row = (TableRow) getChildAt(getChildCount() - 1);
            if (row.getChildCount() >= columnCount) {
                row = createRow();
            }
        }

        // Create a new cell and add it to the row.
        ViewGroup cell = (ViewGroup) View.inflate(getContext(), itemLayout, null);
        cell.setLayoutParams(new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                                                       android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                                                       1.0f));
        disableStateSaving(cell);
        row.addView(cell);
        return cell;
    }

    // I don't want this f*** state to be preserved because inflated items would all be restored
    // with the last cell
    // state (since all items have the same id internaly) during onRestoreInstanceState... which
    // does not even exist
    // when it comes to fragment. (see
    // http://stackoverflow.com/questions/4932179/saving-state-on-compound-view-widgets and
    // http://stackoverflow.com/questions/3542333/how-to-prevent-custom-views-from-losing-state-across-screen-orientation-changes/3542895#3542895).
    private void disableStateSaving(View cell) {
        cell.setSaveEnabled(false);

        if (cell instanceof ViewGroup) {
            ViewGroup cellViewGroup = (ViewGroup) cell;
            int childCount = cellViewGroup.getChildCount();
            for (int i = 0; i < childCount; ++i) {
                disableStateSaving(cellViewGroup.getChildAt(i));
            }
        }
    }

    protected void spanTable() {
        // Create a new row if needed.
        TableRow row;
        if (getChildCount() > 0) {
            row = (TableRow) getChildAt(getChildCount() - 1);
            while (row.getChildCount() < columnCount) {
                FrameLayout emptyCell = new FrameLayout(getContext());
                emptyCell.setLayoutParams(new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                                                                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                                                                    1.0f));
                emptyCell.setTag(this); // Used as an indicator for the padding cell.
                row.addView(emptyCell);
            }
        }
    }

    @Override
    public Iterator<View> iterator() {
        return new Iterator<View>() {
            private int rowCount = getChildCount();
            private int columnCount = Table.this.columnCount;
            private int currentRow = 0;
            private int currentCell = 0;


            @Override
            public boolean hasNext() {
                // After the last row.
                if (currentRow >= rowCount) {
                    return false;
                }
                // Last row. Check for the indicator set in the padding cells.
                else if (currentRow == rowCount - 1) {
                    TableRow tableRow = (TableRow) getChildAt(currentRow);
                    return tableRow.getChildAt(currentCell).getTag() != Table.this;
                }
                // Before the last row.
                else {
                    return true;
                }
            }

            @Override
            public View next() {
                TableRow tableRow = (TableRow) getChildAt(currentRow);
                View view = tableRow.getChildAt(currentCell);

                currentCell = (++currentCell) % columnCount;
                if (currentCell == 0) {
                    ++currentRow;
                }
                return view;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
