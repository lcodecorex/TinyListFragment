## TinyListFragment
带刷新和加载跟多以及自动分页的列表展示的Fragment，Builder方式初始化，使用简单，专注减少列表Fragment的重复代码。API >= 11

亮点：
1. CommonHolder & bindView & notifyChange
2. BaseRecyclerAdapter
3. 懒加载
4. 刷新、加载、缺省
5. 分页机制
6. 布局可定制
7. tiny

## 使用
### gradle添加依赖
```
compile 'com.lcodecorex:tinylistfragment:1.0.1'
```

## 方式一  使用Tiny(Builder)构造一个ListFragment
产生一个列表最简单的方式，不用手动创建fragment（内部有默认adapter，可以直接设置holder）：
```java
public class BookActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment= new Tiny<Book>().hold(new IHolder<Book>() {
            @Override
            public CommonHolder<Book> getHolder(ViewGroup parent) {
                return new BookHolder(parent);
            }
        }).createFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }
}
```

### Tiny中的Builder方法
1. layout(int inflateLayoutId)   非必须

    为Fragment设置layout，不调用此方法会使用默认layout。自定义的layout.xml文件如下：
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.v4.widget.SwipeRefreshLayout
        android:id="@+id/tiny_refreshLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.lcodecorex.LRecyclerView
            android:id="@+id/tiny_recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </android.support.v4.widget.SwipeRefreshLayout>

    <LinearLayout
        android:id="@+id/tiny_emptyView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:orientation="vertical"
        android:visibility="gone">

        <ImageView
            android:id="@+id/iv_tiny_default"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:src="@drawable/empty_share" />
    </LinearLayout>
</FrameLayout>
```

- 包含SwipeRefreshLayout，**id必须为tiny_refreshLayout**
- 包含LRecyclerView，**id必须为tiny_recyclerView**
- 包含用于当缺省页使用的ViewGroup，**id必须为tiny_emptyView**

2. pageSize(int pageItemCount) 单个页面中需要加载的条数，不设置默认为15条
3. setCanLoadMore(boolean canLoadMore)  设置是否允许加载更多，默认为true
4. emptyView(View emptyView)  添加一个缺省页（会替换掉原来的图标和文字）
5. adapter(BaseRecyclerAdapter<T> adapter) 设置适配器（也可以调用hold方法设置ViewHolder，二者使用一个即可）
6. layoutManager(RecyclerView.LayoutManager layoutManager) 设置RecyclerView的LayoutManager
7. hold(IHolder<T> holder) 设置适配器的简单方法，添加Holder使用InnerAdapter在内部添加了适配器
8. enableResumeRefresh(boolean enableResumeRefresh)  是否需要在onResume()时刷新页面，默认为false
9. enableEmptyView(boolean enableEmptyView) 是否需要显示缺省界面，默认为true
10. setSwipeColorRes(int... colorResIds) 设置SwipeRefreshLayout 圆环的颜色，注意是id值，不是16进制值
11. emptyText(String emptyText) 缺省文字
12. emptyImage(int drawableId)  缺省图标资源
13. refreshTimeout(int refreshTimeout)  刷新允许的超时时间（之后会结束刷新控件的显示）
14. loadMoreTimeout(int loadMoreTimeout) 加载更多允许的超时时间
15. lazyLoad()  

   懒加载模式，注意只有在ViewPager中使用才有效，或者在合适的地方手动调用**fragment.setUserVisibleHint(true)**

16. setData(IGetData<T> getDataImpl) 

    获取资源的回调方法，必须要回调才能让系统有效的进行分页判断。
- toRefresh(DataCallback<List<Bean>> callback)  执行获取数据的业务并使用callback回调结果；数据数量少于pageSize将自动关闭加载更多；currentPage不为1时，onResume时禁止了自动刷新；
- toLoadMore(DataCallback<List<Bean>> callback, int currentPage) 执行加载更多获取数据的业务并使用callback回调结果，currentPage表示的是当前需要加载的页面；当加载错误或失败时currentPage将变为原来的值。数据数量少于pageSize将自动关闭加载更多。

17. createFragment()  创建最终的Fragment


### BaseRecyclerAdapter的使用
```java
public class BookAdapter extends BaseRecyclerAdapter<Book> {

        @Override
        protected CommonHolder<Book> getViewHolder(ViewGroup parent) {
            return new BookHolder(parent);
        }
    }
```
- setHeadHolder(View itemView) 方法，可以添加一个Header

当然可以继续重写getItemViewType来支持组建更多不同类型的View。

### CommonHolder的使用
```java
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
```

--- 抽象方法
- 在 bindView() 方法中使用bind(int id)方法为控件绑定id
- 在 bindData(T t) 方法中绑定数据

--- 工具方法
- getActivity/getContext() 获取当前Activity/Context，可以借此来跳转页面
- notifyChange() 通知Adapter刷新下数据

由此可以看出，使用TinyListFragment在很多时候或者说简单列表页面中已经“不需要”Adapter了。

## 方式二 继承及自定制
```java
public class BookListFragment extends TinyListFragment<Book> implements IGetData<Book> {

    @Override
    public Tiny<Book> getConfig() {
        return null;
    }

    @Override
    public void toRefresh(DataCallback<List<Book>> callback) {
        List<Book> books = new ArrayList<>();
        callback.onSuccess(books);
    }

    @Override
    public void toLoadMore(DataCallback<List<Book>> callback, int currentPage) {
        callback.onError();
    }

    @Override
    public void onFailure(String errorCode, String msg) {
        super.onFailure(errorCode, msg);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
````


> ps: 
> Contact me: lcodecore@163.com（没有x）