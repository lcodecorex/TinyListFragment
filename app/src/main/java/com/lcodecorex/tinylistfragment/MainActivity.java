package com.lcodecorex.tinylistfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcodecorex.BaseRecyclerAdapter;
import com.lcodecorex.CommonHolder;
import com.lcodecorex.tiny.IHolder;
import com.lcodecorex.Tiny;
import com.lcodecorex.tiny.DataCallback;
import com.lcodecorex.tiny.IGetData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> fragments = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragments.get(0)).commit();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragments.get(1)).commit();
                    return true;
                case R.id.navigation_notifications:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragments.get(2)).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragments.add(new Tiny<Book>().layout(R.layout.fragment_test).hold(new IHolder<Book>() {
            @Override
            public CommonHolder<Book> getHolder(ViewGroup parent) {
                return new BookHolder(parent);
            }
        }).setCanLoadMore(true).setData(new IGetData<Book>() {
            @Override
            public void toRefresh(DataCallback<List<Book>> callback) {
                List<Book> list = new ArrayList<Book>();
                for (int i = 0; i < 5; i++) {
                    list.add(new Book("张三", "zhangsan@163.com"));
                }
                for (int i = 0; i < 5; i++) {
                    list.add(new Book("李四", "lisi.mail@163.com"));
                }
                for (int i = 0; i < 5; i++) {
                    list.add(new Book("王五", "wangwu@163.com"));
                }
                callback.onSuccess(list);
            }

            @Override
            public void toLoadMore(DataCallback<List<Book>> callback, int currentPage) {
                List<Book> list = new ArrayList<Book>();
                for (int i = 0; i < 5; i++) {
                    list.add(new Book("赵六", "zhangsan@163.com"));
                }
                for (int i = 0; i < 5; i++) {
                    list.add(new Book("王二麻子", "wangermazi@163.com"));
                }
                for (int i = 0; i < 5; i++) {
                    list.add(new Book("老顽童", "laowantong@163.com"));
                }
                callback.onSuccess(list);
            }
        }).createFragment());
        fragments.add(new BookListFragment());
        fragments.add(new Tiny<Book>().createFragment());

        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragments.get(0)).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public class BookAdapter extends BaseRecyclerAdapter<Book> {

        @Override
        protected CommonHolder<Book> getViewHolder(ViewGroup parent) {
            return new BookHolder(parent);
        }
    }

    public class BookHolder extends CommonHolder<Book> {

        private TextView tv_name, tv_intro;

        public BookHolder(ViewGroup root) {
            super(root, R.layout.item_test);
        }

        @Override
        public void bindView() {
            tv_name = bind(R.id.tv_name);
            tv_intro = bind(R.id.tv_intro);
        }

        @Override
        public void bindData(Book book) {
            tv_name.setText(book.name);
            tv_intro.setText(book.intro);
        }
    }
}
