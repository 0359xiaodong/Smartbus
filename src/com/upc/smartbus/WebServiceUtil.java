package com.upc.smartbus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class WebServiceUtil
{
	// ����Web Service�������ռ�
	static final String SERVICE_NS = "http://WebXml.com.cn/";
	// ����Web Service�ṩ�����URL
	static final String SERVICE_URL = 
			"http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx";

	// ����Զ�� Web Service��ȡʡ���б�
	public static List<String> getProvinceList()
	{
		/**
		 * ����Զ��Web Service��getRegionProvince����: ����й�ʡ�ݡ�ֱϽ�С���������֮��Ӧ��ID
		 * <ArrayOfString xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns="http://WebXml.com.cn/">
				<string>������,3113</string>
				<string>����,3114</string>
				<string>����,3115</string>
				<string>���ɹ�,3116</string>
				<string>�ӱ�,3117</string>
				<string>����,3118</string>
				<string>ɽ��,3119</string>
				<string>ɽ��,31110</string>
				<string>����,31111</string>
				<string>����,31112</string>
				<string>����,31113</string>
				<string>����,31114</string>
				<string>����,31115</string>
				<string>�ຣ,31116</string>
				<string>����,31117</string>
				<string>����,31118</string>
				<string>�㽭,31119</string>
				<string>����,31120</string>
				<string>����,31121</string>
				<string>����,31122</string>
				<string>�Ĵ�,31123</string>
				<string>�㶫,31124</string>
				<string>����,31125</string>
				<string>����,31126</string>
				<string>����,31127</string>
				<string>�½�,31128</string>
				<string>����,31129</string>
				<string>̨��,31130</string>
				<string>����,311101</string>
				<string>�Ϻ�,311102</string>
				<string>���,311103</string>
				<string>����,311104</string>
				<string>���,311201</string>
				<string>����,311202</string>
				<string>���㵺,311203</string>
				</ArrayOfString>
		 */
		String methodName = "getRegionProvince";   //����й�ʡ�ݡ�ֱϽ�С���������֮��Ӧ��ID
		// ����HttpTransportSE�������,�ö������ڵ���Web Service����
		HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
		ht.debug = true;
		// ʹ��SOAP1.1Э�鴴��Envelop����
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		// ʵ����SoapObject���󣬴�����Ҫ���õ�Web Service�������ռ䣬Web Service������
		SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		//�� soapObject��������ΪSoapSerializationEnvelope����Ĵ���SOAP��Ϣ
		envelope.bodyOut = soapObject;
		/**
		 *  ��Ϊʲô�����վ��ͨ��.NET�����ṩWeb Service�ģ�
		 *  ���������.Net�ṩ��Web Service���ֽϺõļ�����
		 */
		envelope.dotNet = true;
		try
		{
			// ����Web Service
			ht.call(SERVICE_NS + methodName, envelope);
			if (envelope.getResponse() != null)
			{
				// ��ȡ��������Ӧ���ص�SOAP��Ϣ
				SoapObject result = (SoapObject) envelope.bodyIn;
				SoapObject detail = (SoapObject) result.getProperty(methodName
						+ "Result");
				// ������������Ӧ��SOAP��Ϣ��
				return parseProvinceOrCity(detail);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	// ����ʡ�ݻ�ȡ�����б�
	public static List<String> getCityListByProvince(String province)
	{
		/**
		 *  ���õķ���
		 *  ���֧�ֵĳ���/�������ƺ���֮��Ӧ��ID
			���������theRegionCode = ʡ�С�����ID�����ƣ��������ݣ�һά�ַ������顣
			�磺���뱱����theRegionCode:311101�õ��ķ��ؽ��Ϊ��
			<ArrayOfString xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns="http://WebXml.com.cn/">
				<string>����,792</string>
				<string>��ƽ,785</string>
				<string>����,826</string>
				<string>��ɽ,827</string>
				<string>����,752</string>
				<string>��ͷ��,788</string>
				<string>����,751</string>
				<string>ƽ��,756</string>
				<string>˳��,741</string>
				<string>ͨ��,3409</string>
				<string>����,746</string>
				<string>����,742</string>
				<string>����,3408</string>
				<string>��̨,795</string>
				<string>ʯ��ɽ,794</string>
			</ArrayOfString>
		 */
		String methodName = "getSupportCityString";  
		// ����HttpTransportSE�������
		HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
		ht.debug = true;
		// ʵ����SoapObject����
		SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		// ���һ���������
		soapObject.addProperty("theRegionCode", province);
		// ʹ��SOAP1.1Э�鴴��Envelop����
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = soapObject;
		// ������.Net�ṩ��Web Service���ֽϺõļ�����
		envelope.dotNet = true;
		try
		{
			// ����Web Service
			ht.call(SERVICE_NS + methodName, envelope);
			if (envelope.getResponse() != null)
			{
				// ��ȡ��������Ӧ���ص�SOAP��Ϣ
				SoapObject result = (SoapObject) envelope.bodyIn;
				SoapObject detail = (SoapObject) result.getProperty(methodName
						+ "Result");
				// ������������Ӧ��SOAP��Ϣ��
				return parseProvinceOrCity(detail);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	// ������������Ӧ��SOAP��Ϣ��
	private static List<String> parseProvinceOrCity(SoapObject detail)
	{
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < detail.getPropertyCount(); i++)
		{
			// ������ÿ��ʡ��
			result.add(detail.getProperty(i).toString().split(",")[0]);
		}
		return result;
	}
	// ���ݳ��л�ȡ���о����������
	public static SoapObject getWeatherByCity(String cityName)
	{
		/**
		 * �������Ԥ������        �������������/����ID�����ƣ��������ݣ�һά�ַ������顣
		       �磺����theCityCode��792��<string>����,792</string>���õ��ķ��ؽ��Ϊ��
		       This XML file does not appear to have any style information associated with it. The document tree is shown below.
				<ArrayOfString xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns="http://WebXml.com.cn/">
						<string>ֱϽ�� ����</string>
						<string>����</string>
						<string>792</string>
						<string>2013/04/30 03:47:53</string>
						<string>��������ʵ�������£�14�棻����/���������� 2����ʪ�ȣ�21%</string>
						<string>��������������������ǿ�ȣ�ǿ</string>
						<string>
						����ָ���������ű�����װ�ȴ������װ������������������װ������ҹ�²�ϴ�ע���ʵ������·��� ����ָ�����������������շ��������׹�����Ⱥ�����������������˴����³��㲢������۾��Ϳ��֣��������ʱ��ʱ����ֺͿڱǡ� �˶�ָ���������Ϻã������ڷ����ϴ��Ƽ��������ڽ��е�ǿ���˶������ڻ����˶���ע��ܷ硣 ϴ��ָ��������ϴ����δ�������������������Ϻã��ʺϲ�ϴ������������ơ���������������ĳ������սྻ�� ��ɹָ������������������ɹ���Ͻ��Ѿ�δ�������������������һ��̫����ζ���ɣ� ����ָ���������Ϻã����Դ󣬵��¶����ˣ��Ǹ�������Ŷ�����������Σ������Ծ�������ܴ���Ȼ�����޷�⡣ ·��ָ���������Ϻã�·��Ƚϸ��·���Ϻá� ���ʶ�ָ��������������ã������������������£���о�������ˬ�����ʣ����ƫ�ȡ� ������Ⱦָ�����������������ڿ�����Ⱦ��ϡ�͡���ɢ���������������������� ������ָ���������߷���ǿ������Ϳ��SPF20���ҡ�PA++�ķ�ɹ����Ʒ��������10����14�㱩¶���չ��¡�
						</string>
						<string>4��30�� ��</string>
						<string>11��/27��</string>
						<string>����3-4��ת�޳�������΢��</string>
						<string>0.gif</string>
						<string>0.gif</string>
						<string>5��1�� ��ת����</string>
						<string>12��/25��</string>
						<string>�޳�������΢��</string>
						<string>0.gif</string>
						<string>1.gif</string>
						<string>5��2�� ����ת��</string>
						<string>13��/26��</string>
						<string>�޳�������΢��</string>
						<string>1.gif</string>
						<string>0.gif</string>
						<string>5��3�� ����ת��</string>
						<string>11��/23��</string>
						<string>�޳�������΢��</string>
						<string>1.gif</string>
						<string>2.gif</string>
						<string>5��4�� ��ת����</string>
						<string>14��/27��</string>
						<string>�޳�������΢��</string>
						<string>2.gif</string>
						<string>1.gif</string>											
				</ArrayOfString>

		 */
		String methodName = "getWeather";
		HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		soapObject.addProperty("theCityCode", cityName);
		envelope.bodyOut = soapObject;
		// ������.Net�ṩ��Web Service���ֽϺõļ�����
		envelope.dotNet = true;
		try
		{
			ht.call(SERVICE_NS + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			SoapObject detail = (SoapObject) result.getProperty(methodName
					+ "Result");
			return detail;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
