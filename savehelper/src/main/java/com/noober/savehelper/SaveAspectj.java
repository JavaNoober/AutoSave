package com.noober.savehelper;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by xiaoqi on 2018/3/13
 */

@Aspect
public class SaveAspectj {

    @After("execution(* android.app.Activity.onCreate(..))")
    public void recover(JoinPoint joinPoint) throws Throwable {
        if(joinPoint.getArgs().length == 1){
            SaveHelper.recover(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0]);
        }else if(joinPoint.getArgs().length == 2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            SaveHelper.recover(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0], (PersistableBundle) joinPoint.getArgs()[1]);
        }

    }

    @Before("execution(* android.app.Activity.onSaveInstanceState(..))")
    public void save(JoinPoint joinPoint) throws Throwable {
        if(joinPoint.getArgs().length == 1){
            SaveHelper.save(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0]);
        }else if(joinPoint.getArgs().length == 2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            SaveHelper.save(joinPoint.getThis(), (Bundle) joinPoint.getArgs()[0], (PersistableBundle) joinPoint.getArgs()[1]);
        }

    }
}
