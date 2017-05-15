package com.onlypxz.web.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.onlypxz.web.entity.SmsInfo;
import com.sun.prism.Image;

import zp.base.bean.ExecuteParam;
import zp.base.dao.DaoImpl;
import zp.base.service.Service;
import zp.base.utils.CollectionUtils;
import zp.base.utils.SqlHelper;
import zp.base.utils.SqlHelper.ExecuteType;

public class SmsService extends Service<SmsInfo>{

	@Override
	public List<SmsInfo> query() {
		DaoImpl impl=new DaoImpl();
		String sql="select "+SqlHelper.getUpdateParam(ExecuteType.QUERY, new SmsInfo()).getParamNames()+" from SmsInfo";
		return impl.query(SmsInfo.class, sql);
	}

	@Override
	public SmsInfo queryById(String id) {
		DaoImpl impl=new DaoImpl();
		String sql="select "+SqlHelper.getUpdateParam(ExecuteType.QUERY, new SmsInfo(), "pwd").getParamNames()+" from SmsInfo where id=?";
		return impl.querySingle(SmsInfo.class, sql, new String[]{id});
	}

	@Override
	public boolean delete(SmsInfo t) {
		DaoImpl impl=new DaoImpl();
		String sql="delete from SmsInfo where id=?";
		return impl.update(SmsInfo.class, sql, new String[]{t.getId()+""});
	}

	@Override
	public boolean add(SmsInfo t) {
		DaoImpl impl=new DaoImpl();
		ExecuteParam param=SqlHelper.getUpdateParam(ExecuteType.ADD,t, "id");
		String sql="insert into SmsInfo"+param.getParamNames();
		List<String> paramValues=param.getParamVals();
		return impl.update(SmsInfo.class, sql, CollectionUtils.listToArray(paramValues));
	}

	@Override
	public boolean update(SmsInfo t) {
		DaoImpl impl=new DaoImpl();
		ExecuteParam param=SqlHelper.getUpdateParam(ExecuteType.MODIFY,t, "id");
		String sql="update SmsInfo set "+param.getParamNames()+" where id=?";
		List<String> paramValues=param.getParamVals();
		paramValues.add(t.getId()+"");
		return impl.update(SmsInfo.class, sql, CollectionUtils.listToArray(paramValues));
	}
	
	public boolean addAll(List<SmsInfo> smsInfos) {
		if(CollectionUtils.isEmpty(smsInfos))
			return false;
		DaoImpl impl=new DaoImpl();
		String sql="insert into SmsInfo";
		List<String> paramValues=new ArrayList<>();
		for (int i = 0; i < smsInfos.size(); i++) {
			SmsInfo si=smsInfos.get(i);
			ExecuteParam param=SqlHelper.getUpdateParam(ExecuteType.ADD,si,"id");
			String params=param.getParamNames();
			paramValues.addAll(param.getParamVals());
			if(i==0)
			{
				sql+=params;
				continue;
			}
			sql+=","+params.substring(params.indexOf("values")+"values".length());
			
		}
//		System.out.println(sql);
//		System.out.println(paramValues);
		return impl.update(SmsInfo.class, sql, CollectionUtils.listToArray(paramValues));
	}
	public boolean deleteAll()
	{
		String sql="delete from SmsInfo";
		return SqlHelper.getInstance().updateExecute(sql,null)>0;
	}

}
