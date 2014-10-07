package com.upc.smartbus;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;  
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;  
import org.ksoap2.transport.HttpTransportSE;
public class ConnectService {
	private   String serviceurl="http://192.168.191.1:8080/axis2/services/Upload_DownloadPhoto?wsdl";
	private   String NameSpace="http://db.upc";


	//下面这个方法用来上传照片到服务器
	public String uploadPhoto(String photoname,String image,String location,String user_des){
		String methodName="uploadPhoto";
		SoapObject request=new SoapObject(NameSpace,methodName);
		request.addProperty("photoname",photoname);
		request.addProperty("image", image);
		request.addProperty("user_description", user_des);
		request.addProperty("location", location);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);//创建SoapSerializationEnvelope对象，并指定WebService的版本 
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(serviceurl);//创建HttpTransportSE对象，并指定WSDL文档的URL
		try{
			ht.call(NameSpace+methodName, envelope);
			if(envelope.getResponse()!=null){
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();//使用getResponse方法获得WebService方法的返回结果
				return response.toString();
			}
			else{
				return "上传失败";
			}
		}
		catch(Exception e){
			return"上传失败";
		}
	}


	public String downloadphoto(String photoName,String methodName){//下载图片的方法。调用下载小图的方法。
		SoapObject request=new SoapObject(NameSpace,methodName);
		request.addProperty("name", photoName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);//创建SoapSerializationEnvelope对象，并指定WebService的版本 
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(serviceurl);//创建HttpTransportSE对象，并指定WSDL文档的URL
		try{
			ht.call(NameSpace+methodName, envelope);
			if(envelope.getResponse()!=null){
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();//使用getResponse方法获得WebService方法的返回结果
				return response.toString();
			}
			else{
				return "webservice连接失败";
			}
		}
		catch(Exception e){
			return"webservice连接失败";
		}
	}
	//返回照片名称
	public String getPhotoName(String cityName){
		String methodName="returnPhotoName";
		SoapObject request=new SoapObject(NameSpace,methodName);
		request.addProperty("cityName", cityName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);//创建SoapSerializationEnvelope对象，并指定WebService的版本 
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(serviceurl);//创建HttpTransportSE对象，并指定WSDL文档的URL
		try{
			ht.call(NameSpace+methodName, envelope);
			if(envelope.getResponse()!=null){
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();//使用getResponse方法获得WebService方法的返回结果
				return response.toString();
			}
			else{
				return "webservice连接失败";
			}
		}
		catch(Exception e){
			return"webservice连接失败";
		}
	}
	/*
	 * 这个方法用来从数据库获取照片相关信息
	 */
	public String getPhotoInfo(String PhotoName){
		String methodName="getPhotoInfo";
		SoapObject request=new SoapObject(NameSpace,methodName);
		request.addProperty("photoname", PhotoName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);//创建SoapSerializationEnvelope对象，并指定WebService的版本 
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(serviceurl);//创建HttpTransportSE对象，并指定WSDL文档的URL
		try{
			ht.call(NameSpace+methodName, envelope);
			if(envelope.getResponse()!=null){
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();//使用getResponse方法获得WebService方法的返回结果
				return response.toString();
			}
			else{
				return "webservice连接失败";
			}
		}
		catch(Exception e){
			return"webservice连接失败";
		}
	}
	public String getLcation(String pname){
		String methodName="getLocation";
		SoapObject request=new SoapObject(NameSpace,methodName);
		request.addProperty("pname", pname);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);//创建SoapSerializationEnvelope对象，并指定WebService的版本 
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(serviceurl);//创建HttpTransportSE对象，并指定WSDL文档的URL
		try{
			ht.call(NameSpace+methodName, envelope);
			if(envelope.getResponse()!=null){
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();//使用getResponse方法获得WebService方法的返回结果
				return response.toString();
			}
			else{
				return "webservice连接失败";
			}
		}
		catch(Exception e){
			return"webservice连接失败";
		}
	}
	

}
