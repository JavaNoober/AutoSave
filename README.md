[![license](https://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat)](https://github.com/JavaNoober/AutoSave)
[![JCenter](https://img.shields.io/badge/JCenter-AutoSaver-green.svg?style=flat)](https://bintray.com/noober/maven/AutoSaver)
## AutoSave
该框架可以自动生成OnSaveInstanceState代码，保持内存恢复，支持kotlin使用


    版本更新说明：
    
    1.0.0 完成基本功能;
    1.0.1 全局变量的作用域从之前强制public改成只要非private即可;
    1.0.2 修改 SaveHelper.bind(this, savedInstanceState)方法为SaveHelper.recover(this, savedInstanceState),只是重命名，
          以便于理解;
          去掉当内存被回收去调用recover方法时，却没有对应helper类会主动抛异常的情况,方便在BaseAcitviy 和 BaseFragment的
          onSaveInstanceState 和 onRestoreInstanceState 统一添加SaveHelper.save和SaveHelper.recover方法。
    1.0.3 优化代码生成,如果一个activity或者fragment中没有有效的@NeedSave注解，但是添加了SaveHelper.recover和SaveHelper.save
          方法，现在就不会自动生成这个类的SaveStateHelper类，减少了无用SaveStateHelper类，便于在Base类中统一集成。
          
    2.0.0 去掉NeedSave注解中的isParcelable字段，自动可以支持不同类型;
          如果字段被标记为private在编译的时候会抛异常;
          支持基本所有bundle可以传入的类型,包括SparseParcelableArray等, 如果传入的类型bundle不支持会抛异常（如果有遗漏的类型，请在github 提出issue）;
    2.0.2 修复通过继承去实现Serializable的对象不能识别的bug;
    2.0.3 优化异常提示
    2.0.4 修复枚举类型保存的时候不能识别的问题
    
    2.1.0 增加对PersistableBundle的支持,NeedSave注解中设置isPersistable = true则说明该参数保存到PersistableBundle
    2.2.6 增加对自定义view的数据保存以及恢复
    3.0.0 增加autosave plugin，省去SaveHelper.save 和 SaveHelper.recover的调用
    3.0.6 简化引入方式
    3.0.7 添加对kotlin的支持

## 引入方式  
在project的gradle加入下面的依赖：

    dependencies {
        ...
        classpath 'com.noober.save:plugin:3.0.7'
    }

在app的gradle或者module的gradle中加入下面插件即可：

    apply plugin: 'AutoSave'

如果需要支持kotlin:  
project的gradle:

    dependencies {
        ...
        classpath 'com.noober.save:plugin:3.0.7'
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.2'
    }


在app的gradle或者module的gradle:
    
    apply plugin: 'kotlin-kapt'
    apply plugin: 'kotlin-android-extensions'
    apply plugin: 'kotlin-android'
    apply plugin: 'AutoSave'
    apply plugin: 'android-aspectjx'
    
## 混淆配置：

     -dontwarn  com.noober.**
     -keep class com.noober.api.**{*;}
     -keep class com.noober.savehelper.**{*;}
     -keep class * implements com.noober.savehelper.ISaveInstanceStateHelper {*;}
     
## 使用方法

对变量增加@NeedSave注解即可

### Activity 和 Fragment

_**注意：**_   
1.Activity和Fragment中使用的时候必须重写一下**onSaveInstanceState方法**，或者在父类BaseActivity和BaseFragment
中**重写一次**即可，否则保存数据的代码会注入失败  
2.如果想要自己定义内存恢复的位置可以使用**SaveHelper.recover**方法
3.如果要在kotlin使用，与在java中使用相同，直接加注解即可，但是不同之出在于：    
  4.1：如果是基本数据类型，需要多添加一个注解@JvmField
  4.2：如果是其他数据类型，需要增加lateinit关键字或者添加一个注解@JvmField
  否则会报错"the modifier of the field must not be private, otherwise  it won't work"。

    
    public class MainActivity extends AppCompatActivity {
    
        @NeedSave
        int a = 0;
        TextView textView;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            textView = findViewById(R.id.tv);
            textView.setText(a + "");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    a = 2222;
                    textView.setText(a + "");
                }
            });
        }
    
    
        @Override
        protected void onResume() {
            super.onResume();
            textView.setText(a + "");
            Log.e("MainActivity", a + "");
        }
    
        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
        }
    }

    
    public class BlankFragment extends Fragment {
        
        @NeedSave
        String mParam1;
    
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
    
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_blank, container, false);
        }
    
        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
        }
    }
    
    class KotlinActivity : AppCompatActivity() {
    
        @NeedSave
        @JvmField
        var a :Int=3
    
        @NeedSave
        lateinit var bundle: Bundle
    
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_kotlin)
            Log.e("KotlinActivity",  a.toString())
    
        }
    
    
        override fun onSaveInstanceState(outState: Bundle?) {
            Log.e("KotlinActivity",  "onSaveInstanceState")
            a = 2
            super.onSaveInstanceState(outState)
        }
    }
    
### 自定义view

    public class CustomView extends View {
            
        @NeedSave
        int a;
    
        public CustomView(Context context) {
            super(context);
        }
    
        public CustomView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }
    
    
        public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
    
    
        @Override
        protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
            SaveHelper.save(this, container);
            super.dispatchSaveInstanceState(container);
        }
    
        @Override
        protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
            super.dispatchRestoreInstanceState(container);
            SaveHelper.recover(this, container);
        }
    }

## 具体原理介绍
  
  [**原理介绍**](https://github.com/JavaNoober/AutoSave/blob/master/README-PRINCIPLE.md)
   
