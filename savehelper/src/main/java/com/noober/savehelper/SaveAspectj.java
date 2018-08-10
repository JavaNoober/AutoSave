package com.noober.savehelper;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by xiaoqi on 2018/8/9
 */

@Aspect
public class SaveAspectj {

    @After("execution(* android.app.Activity.onCreate(..))")
    public void recoverForActivity(JoinPoint joinPoint) throws Throwable {
        if(joinPoint.getArgs().length == 1){
            SaveHelper.recover(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0]);
        }else if(joinPoint.getArgs().length == 2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            SaveHelper.recover(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0], (PersistableBundle) joinPoint.getArgs()[1]);
        }
    }

    @After("execution(* android.app.Fragment.onCreateView(..))")
    public void recoverForFragment(JoinPoint joinPoint) throws Throwable {
        if(joinPoint.getArgs().length == 1){
            SaveHelper.recover(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0]);
        }else if(joinPoint.getArgs().length == 2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            SaveHelper.recover(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0], (PersistableBundle) joinPoint.getArgs()[1]);
        }
    }

    @After("execution(* android.support.v4.app.Fragment.onCreateView(..))")
    public void recoverForFragmentV4(JoinPoint joinPoint) throws Throwable {
        if(joinPoint.getArgs().length == 1){
            SaveHelper.recover(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0]);
        }else if(joinPoint.getArgs().length == 2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            SaveHelper.recover(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0], (PersistableBundle) joinPoint.getArgs()[1]);
        }
    }

    @Before("execution(* android.app.Activity.onSaveInstanceState(..))")
    public void saveForActivity(JoinPoint joinPoint) throws Throwable {
        if(joinPoint.getArgs().length == 1){
            SaveHelper.save(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0]);
        }else if(joinPoint.getArgs().length == 2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            SaveHelper.save(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0], (PersistableBundle) joinPoint.getArgs()[1]);
        }
    }

    @Before("execution(* android.support.v4.app.Fragment.onSaveInstanceState(..))")
    public void saveForFragmentV4(JoinPoint joinPoint) throws Throwable {
        if(joinPoint.getArgs().length == 1){
            SaveHelper.save(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0]);
        }else if(joinPoint.getArgs().length == 2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            SaveHelper.save(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0], (PersistableBundle) joinPoint.getArgs()[1]);
        }
    }

    @Before("execution(* android.app.Fragment.onSaveInstanceState(..))")
    public void saveForFragment(JoinPoint joinPoint) throws Throwable {
        if(joinPoint.getArgs().length == 1){
            SaveHelper.save(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0]);
        }else if(joinPoint.getArgs().length == 2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            SaveHelper.save(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0], (PersistableBundle) joinPoint.getArgs()[1]);
        }
    }
}
