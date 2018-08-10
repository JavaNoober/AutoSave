//package com.recover.autosavesample;
//
//import android.util.Log;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//
///**
// * Created by xiaoqi on 2018/3/13
// */
//
//@Aspect
//public class SaveAspectj {
//
//	public static final String TAG = "Statistics";
//
////	@Around("execution(@com.noob.databurialpoint.Statistics * *(..)) && @annotation(statistics)")
////	public void aroundJoinPoint(ProceedingJoinPoint joinPoint, Statistics statistics) throws Throwable {
////		calculate(statistics);
////		joinPoint.proceed();//执行原方法
////	}
////
////	private void calculate(Statistics statistics){
////		if(statistics != null){
////			Log.e(TAG, "对" + statistics.function().getFunctionName() + "进行统计");
////			// select * from FunctionsTable where operatorId=statistics.getFunctionId()
////			//if(size > 0){
////			// int counts = operateCounts ++
////			// update FunctionsTable set operateCounts = counts
////			// }else {
////			// insert into FunctionsTable values (xxx, statistics.getFunctionId(), 1)
////			// }
////		}
////	}
//
//	@Around("execution(* android.app.Activity.onCreate(..))")
//	public void onClickLitener(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
////        Log.e(TAG, "OnClick");
//        Log.e(TAG, "OnClick");
//        proceedingJoinPoint.proceed();
//	}
//}
