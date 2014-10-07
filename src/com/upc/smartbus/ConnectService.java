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


	//����������������ϴ���Ƭ��������
	public String uploadPhoto(String photoname,String image,String location,String user_des){
		String methodName="uploadPhoto";
		SoapObject request=new SoapObject(NameSpace,methodName);
		request.addProperty("photoname",photoname);
		request.addProperty("image", image);
		request.addProperty("user_description", user_des);
		request.addProperty("location", location);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);//����SoapSerializationEnvelope���󣬲�ָ��WebService�İ汾 
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(serviceurl);//����HttpTransportSE���󣬲�ָ��WSDL�ĵ���URL
		try{
			ht.call(NameSpace+methodName, envelope);
			if(envelope.getResponse()!=null){
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();//ʹ��getResponse�������WebService�����ķ��ؽ��
				return response.toString();
			}
			else{
				return "�ϴ�ʧ��";
			}
		}
		catch(Exception e){
			return"�ϴ�ʧ��";
		}
	}


	public String downloadphoto(String photoName,String methodName){//����ͼƬ�ķ�������������Сͼ�ķ�����
		SoapObject request=new SoapObject(NameSpace,methodName);
		request.addProperty("name", photoName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);//����SoapSerializationEnvelope���󣬲�ָ��WebService�İ汾 
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(serviceurl);//����HttpTransportSE���󣬲�ָ��WSDL�ĵ���URL
		try{
			ht.call(NameSpace+methodName, envelope);
			if(envelope.getResponse()!=null){
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();//ʹ��getResponse�������WebService�����ķ��ؽ��
				return response.toString();
			}
			else{
				return "webservice����ʧ��";
			}
		}
		catch(Exception e){
			return"webservice����ʧ��";
		}
	}
	//������Ƭ����
	public String getPhotoName(String cityName){
		String methodName="returnPhotoName";
		SoapObject request=new SoapObject(NameSpace,methodName);
		request.addProperty("cityName", cityName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);//����SoapSerializationEnvelope���󣬲�ָ��WebService�İ汾 
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(serviceurl);//����HttpTransportSE���󣬲�ָ��WSDL�ĵ���URL
		try{
			ht.call(NameSpace+methodName, envelope);
			if(envelope.getResponse()!=null){
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();//ʹ��getResponse�������WebService�����ķ��ؽ��
				return response.toString();
			}
			else{
				return "webservice����ʧ��";
			}
		}
		catch(Exception e){
			return"webservice����ʧ��";
		}
	}
	/*
	 * ����������������ݿ��ȡ��Ƭ�����Ϣ
	 */
	public String getPhotoInfo(String PhotoName){
		String methodName="getPhotoInfo";
		SoapObject request=new SoapObject(NameSpace,methodName);
		request.addProperty("photoname", PhotoName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);//����SoapSerializationEnvelope���󣬲�ָ��WebService�İ汾 
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(serviceurl);//����HttpTransportSE���󣬲�ָ��WSDL�ĵ���URL
		try{
			ht.call(NameSpace+methodName, envelope);
			if(envelope.getResponse()!=null){
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();//ʹ��getResponse�������WebService�����ķ��ؽ��
				return response.toString();
			}
			else{
				return "webservice����ʧ��";
			}
		}
		catch(Exception e){
			return"webservice����ʧ��";
		}
	}
	public String getLcation(String pname){
		String methodName="getLocation";
		SoapObject request=new SoapObject(NameSpace,methodName);
		request.addProperty("pname", pname);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);//����SoapSerializationEnvelope���󣬲�ָ��WebService�İ汾 
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(serviceurl);//����HttpTransportSE���󣬲�ָ��WSDL�ĵ���URL
		try{
			ht.call(NameSpace+methodName, envelope);
			if(envelope.getResponse()!=null){
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();//ʹ��getResponse�������WebService�����ķ��ؽ��
				return response.toString();
			}
			else{
				return "webservice����ʧ��";
			}
		}
		catch(Exception e){
			return"webservice����ʧ��";
		}
	}
	

}
