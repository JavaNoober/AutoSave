[![license](https://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat)](https://github.com/didi/VirtualAPK/blob/master/LICENSE)
[![JCenter](https://img.shields.io/badge/JCenter-AutoSaver-green.svg?style=flat)](https://bintray.com/noober/maven/AutoSaver)


    版本更新说明：
    
    1.0.0 完成基本功能;
    1.0.1 全局变量的作用域从之前强制public改成只要非private即可;
    1.0.2 修改 SaveHelper.bind(this, savedInstanceState)方法为SaveHelper.recover(this, savedInstanceState),只是重命名，
          以便于理解;
          去掉当内存被收回去调用recover方法时，却没有对应helper类会主动抛异常的情况,方便在BaseAcitviy 和 BaseFragment的
          onSaveInstanceState 和 onRestoreInstanceState 统一添加SaveHelper.save和SaveHelper.recover方法。


引入方式,在app的gradle中加入下面依赖即可：


    compile 'com.noober:savehelper:1.0.2'
    compile 'com.noober:savehelper-api:1.0.2'
    annotationProcessor 'com.noober:processor:1.0.2'

# 引入

android 内存被回收是一个开发者的常见问题。当我们**跳转到一个二级界面，或者切换到后台**的时候，如果时间过长或者手机的**内存不足**，当我们再返回这个界面的时候，activity或fragment就会被内存回收。这时候虽然界面被重新执行了onCreate，但是很多变量的值却已经被置空，这样就导致了很多潜在的bug，已经很多空指针的问题。

其实这种问题需要解决的话也很简单。大家知道，当Activity或者Fragment被内存回收后，我们再进入这个界面，它会自动重新进行onCreate操作，并且系统会帮助我们保存一些值。但是系统只会保存界面上的一些元素，比如textview中的文字，但是很多全局变量仍然会被置空。
对于保存这些变量，我们可以重写**onSaveInstanceState**这个方法，在onCreate中即可恢复数据。代码如下：
    |

    int a;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		//内存回收，界面重新onCreate后，恢复数据
		if(savedInstanceState != null){
			a = savedInstanceState.getInt("A");
		}
	}

	private void initData() {
		...
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//保存数据
		outState.putInt("A", a);
		super.onSaveInstanceState(outState);
	}

通过这样的操作，便可以解决内存回收后变量a的值变为初始值0的问题。

问题到这里，似乎已经可以解决内存被回收的问题了。但是随着项目的开发，一个Activity中的变量以及**代码会变得非常多**，这时候我们需要去保存某个值就会使代码变得越来越凌乱，同时不断重复的去写outState.putXX已经savedInstanceState.getXX这样的代码都是很重复的，一不小心还会去写错中间的key值。

于是我写了这个很轻量级的框架，来解决这个问题。先给出引入这个框架后的代码写法：

    @NeedSave
	String test;
	@NeedSave
	protected boolean b;
	@NeedSave
	public Boolean c;
	@NeedSave
	public ArrayList<String> t;
	@NeedSave
	public Integer i;
	@NeedSave(isParcelable = true)
	public ParcelableObject example;
	@NeedSave
	public SerializableObject example;
	@NeedSave
	public Float f1;
	@NeedSave
	public float f2;
	@NeedSave
	public char achar;
	@NeedSave
	public char achars[];
	@NeedSave
	public int sssss[];
	@NeedSave
	public Bundle bundle;
	@NeedSave
	public int a;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		SaveHelper.recover(this,savedInstanceState);
	}

	private void initData() {
		//TODO
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		SaveHelper.save(this,outState);
		super.onSaveInstanceState(outState);
	}

这里我特地写了很多的变量，但是无论这个Activity中有多少变量，我在onCreate和onSaveInstanceState代码中都只要去各写一行代码,同时给变量加一个标签标记一个即可：

        @NeedSave
        SaveHelper.recover(this,savedInstanceState);
        SaveHelper.save(this,outState);

这样就不会因为这种太多的重复的操作去导致代码逻辑的混乱,同时也避免了敲代码时因为key写错导致的错误。

# 效果展示
我们来看一下测试代码：
## 不进行数据保存操作

![](https://user-gold-cdn.xitu.io/2017/12/14/16053bf7ed7fb8f7?w=744&h=621&f=png&s=71616)
很简单，就是通过点击事情，去给变量“testString”赋值，然后再去模拟内存被回收的情况，看一下显示的值是否是内存被回收前的。

![](https://user-gold-cdn.xitu.io/2017/12/14/16053ce960cd9ee3?w=416&h=585&f=gif&s=522365)

## 调用框架代码后的内存恢复
加入框架代码:

![](https://user-gold-cdn.xitu.io/2017/12/14/16053c324e7de04c?w=696&h=787&f=png&s=95432)

加入代码之后的效果：


![](https://user-gold-cdn.xitu.io/2017/12/14/16053cf18fe46d78?w=418&h=605&f=gif&s=450838)





# 原理介绍

## @NeedSave

这是一个注解，这个注解只能使用在全局变量中，特别注意，~~被加上这个注解的变量必须是**public**，否则会不生效~~。
1.0.1更新为只要非private即可。

当前支持保存的类型有：

        String
        boolean Boolean
        ArrayList
        int int[] Integer
        Parcelable
        Serializable
        float Float
        char[] char
        Bundle

    注意，如果是Parcelable类型，需要特别在注解中加入	@NeedSave(isParcelable = true) 这样标记
## SaveHelper.recover(this,savedInstanceState);
这个方法其实是恢复数据的时候去调用的。

	public static <T> void recover(T recover, Bundle savedInstanceState){
		if(savedInstanceState != null){
			ISaveInstanceStateHelper<T> saveInstanceStateHelper = findSaveHelper(recover);
			if(saveInstanceStateHelper != null){
				saveInstanceStateHelper.recover(savedInstanceState, recover);
			}
		}
	}

savedInstanceState不会null的时候，说明就是需要内存恢复的时候，这时候就会去通过findSaveHelper方法找到一个实现类，然后去调用recover方法恢复数据。
## SaveHelper.save(this,outState);
这是一个保存数据的方法，**注意**的是，这个方法必须在super.onSaveInstanceState(outState);之前调用。

        	public static <T> void save(T save, Bundle outState){
        		ISaveInstanceStateHelper<T> saveInstanceStateHelper = findSaveHelper(save);
        		if(saveInstanceStateHelper != null){
        			saveInstanceStateHelper.save(outState, save);
        		}
        	}

它最终调用的是ISaveInstanceStateHelper实现类的save方法。

## ISaveInstanceStateHelper实现类
这个类是一个接口，专门用来保存和恢复数据用。这个类是不要我们自己写的，在代码编译的时候会**自动生成**模板代码。整个调用过程中也只有寻找ISaveInstanceStateHelper实现类的findSaveHelper这个方法调用了反射，其他时候不会去用到反射，而影响效率。
自动生成代码所在位置：

![](https://user-gold-cdn.xitu.io/2017/12/9/1603a6d7496e99bf?w=465&h=275&f=png&s=15537)

自动生成的代码如下：

    public class MainActivity_SaveStateHelper implements ISaveInstanceStateHelper<MainActivity> {
      @Override
      public void save(Bundle outState, MainActivity save) {
        outState.putString("TEST",save.test);
        outState.putBoolean("C",save.c);
        outState.putSerializable("T",save.t);
        outState.putInt("I",save.i);
        outState.putParcelable("EXAMPLE",save.example);
        outState.putFloat("F1",save.f1);
        outState.putFloat("F2",save.f2);
        outState.putChar("ACHAR",save.achar);
        outState.putCharArray("ACHARS",save.achars);
        outState.putIntArray("SSSSS",save.sssss);
        outState.putIntArray("SASA",save.sasa);
        outState.putBundle("BUNDLE",save.bundle);
        outState.putInt("A",save.a);
      }

      @Override
      public void recover(Bundle savedInstanceState, MainActivity recover) {
        if(savedInstanceState != null) {
          recover.test = savedInstanceState.getString("TEST");
          recover.c = savedInstanceState.getBoolean("C");
          recover.t = (ArrayList<String>)savedInstanceState.getSerializable("T");
          recover.i = savedInstanceState.getInt("I");
          recover.example = savedInstanceState.getParcelable("EXAMPLE");
          recover.f1 = savedInstanceState.getFloat("F1");
          recover.f2 = savedInstanceState.getFloat("F2");
          recover.achar = savedInstanceState.getChar("ACHAR");
          recover.achars = savedInstanceState.getCharArray("ACHARS");
          recover.sssss = savedInstanceState.getIntArray("SSSSS");
          recover.sasa = savedInstanceState.getIntArray("SASA");
          recover.bundle = savedInstanceState.getBundle("BUNDLE");
          recover.a = savedInstanceState.getInt("A");
        }
      }
    }

# 总结
看到这里大家已经猜到其实这个框架的实现原理和BufferKnife是相同的。而bufferknife的原理很多文章都有，这里就不过多介绍了。

github地址：[https://github.com/JavaNoober/AutoSave](https://github.com/JavaNoober/AutoSave)
   
