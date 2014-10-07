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
	// 定义Web Service的命名空间
	static final String SERVICE_NS = "http://WebXml.com.cn/";
	// 定义Web Service提供服务的URL
	static final String SERVICE_URL = 
			"http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx";

	// 调用远程 Web Service获取省份列表
	public static List<String> getProvinceList()
	{
		/**
		 * 调用远程Web Service的getRegionProvince方法: 获得中国省份、直辖市、地区和与之对应的ID
		 * <ArrayOfString xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns="http://WebXml.com.cn/">
				<string>黑龙江,3113</string>
				<string>吉林,3114</string>
				<string>辽宁,3115</string>
				<string>内蒙古,3116</string>
				<string>河北,3117</string>
				<string>河南,3118</string>
				<string>山东,3119</string>
				<string>山西,31110</string>
				<string>江苏,31111</string>
				<string>安徽,31112</string>
				<string>陕西,31113</string>
				<string>宁夏,31114</string>
				<string>甘肃,31115</string>
				<string>青海,31116</string>
				<string>湖北,31117</string>
				<string>湖南,31118</string>
				<string>浙江,31119</string>
				<string>江西,31120</string>
				<string>福建,31121</string>
				<string>贵州,31122</string>
				<string>四川,31123</string>
				<string>广东,31124</string>
				<string>广西,31125</string>
				<string>云南,31126</string>
				<string>海南,31127</string>
				<string>新疆,31128</string>
				<string>西藏,31129</string>
				<string>台湾,31130</string>
				<string>北京,311101</string>
				<string>上海,311102</string>
				<string>天津,311103</string>
				<string>重庆,311104</string>
				<string>香港,311201</string>
				<string>澳门,311202</string>
				<string>钓鱼岛,311203</string>
				</ArrayOfString>
		 */
		String methodName = "getRegionProvince";   //获得中国省份、直辖市、地区和与之对应的ID
		// 创建HttpTransportSE传输对象,该对象用于调用Web Service操作
		HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
		ht.debug = true;
		// 使用SOAP1.1协议创建Envelop对象
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		// 实例化SoapObject对象，传入所要调用的Web Service的命名空间，Web Service方法名
		SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		//将 soapObject对象设置为SoapSerializationEnvelope对象的传出SOAP消息
		envelope.bodyOut = soapObject;
		/**
		 *  因为什么这个网站是通过.NET对外提供Web Service的，
		 *  因此设置与.Net提供的Web Service保持较好的兼容性
		 */
		envelope.dotNet = true;
		try
		{
			// 调用Web Service
			ht.call(SERVICE_NS + methodName, envelope);
			if (envelope.getResponse() != null)
			{
				// 获取服务器响应返回的SOAP消息
				SoapObject result = (SoapObject) envelope.bodyIn;
				SoapObject detail = (SoapObject) result.getProperty(methodName
						+ "Result");
				// 解析服务器响应的SOAP消息。
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

	// 根据省份获取城市列表
	public static List<String> getCityListByProvince(String province)
	{
		/**
		 *  调用的方法
		 *  获得支持的城市/地区名称和与之对应的ID
			输入参数：theRegionCode = 省市、国家ID或名称，返回数据：一维字符串数组。
			如：输入北京的theRegionCode:311101得到的返回结果为：
			<ArrayOfString xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns="http://WebXml.com.cn/">
				<string>北京,792</string>
				<string>昌平,785</string>
				<string>大兴,826</string>
				<string>房山,827</string>
				<string>怀柔,752</string>
				<string>门头沟,788</string>
				<string>密云,751</string>
				<string>平谷,756</string>
				<string>顺义,741</string>
				<string>通州,3409</string>
				<string>延庆,746</string>
				<string>海淀,742</string>
				<string>朝阳,3408</string>
				<string>丰台,795</string>
				<string>石景山,794</string>
			</ArrayOfString>
		 */
		String methodName = "getSupportCityString";  
		// 创建HttpTransportSE传输对象
		HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
		ht.debug = true;
		// 实例化SoapObject对象
		SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		// 添加一个请求参数
		soapObject.addProperty("theRegionCode", province);
		// 使用SOAP1.1协议创建Envelop对象
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = soapObject;
		// 设置与.Net提供的Web Service保持较好的兼容性
		envelope.dotNet = true;
		try
		{
			// 调用Web Service
			ht.call(SERVICE_NS + methodName, envelope);
			if (envelope.getResponse() != null)
			{
				// 获取服务器响应返回的SOAP消息
				SoapObject result = (SoapObject) envelope.bodyIn;
				SoapObject detail = (SoapObject) result.getProperty(methodName
						+ "Result");
				// 解析服务器响应的SOAP消息。
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

	// 解析服务器响应的SOAP消息。
	private static List<String> parseProvinceOrCity(SoapObject detail)
	{
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < detail.getPropertyCount(); i++)
		{
			// 解析出每个省份
			result.add(detail.getProperty(i).toString().split(",")[0]);
		}
		return result;
	}
	// 根据城市获取城市具体天气情况
	public static SoapObject getWeatherByCity(String cityName)
	{
		/**
		 * 获得天气预报数据        输入参数：城市/地区ID或名称，返回数据：一维字符串数组。
		       如：输入theCityCode：792（<string>北京,792</string>）得到的返回结果为：
		       This XML file does not appear to have any style information associated with it. The document tree is shown below.
				<ArrayOfString xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns="http://WebXml.com.cn/">
						<string>直辖市 北京</string>
						<string>北京</string>
						<string>792</string>
						<string>2013/04/30 03:47:53</string>
						<string>今日天气实况：气温：14℃；风向/风力：东风 2级；湿度：21%</string>
						<string>空气质量：良；紫外线强度：强</string>
						<string>
						穿衣指数：建议着薄型套装等春秋过渡装。年老体弱者宜着套装。但昼夜温差较大，注意适当增减衣服。 过敏指数：天气条件极易诱发过敏，易过敏人群尽量减少外出，外出宜穿长衣长裤并佩戴好眼镜和口罩，外出归来时及时清洁手和口鼻。 运动指数：天气较好，但由于风力较大，推荐您在室内进行低强度运动，若在户外运动请注意避风。 洗车指数：适宜洗车，未来持续两天无雨天气较好，适合擦洗汽车，蓝天白云、风和日丽将伴您的车子连日洁净。 晾晒指数：天气不错，适宜晾晒。赶紧把久未见阳光的衣物搬出来吸收一下太阳的味道吧！ 旅游指数：天气较好，风稍大，但温度适宜，是个好天气哦。很适宜旅游，您可以尽情地享受大自然的无限风光。 路况指数：天气较好，路面比较干燥，路况较好。 舒适度指数：白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。 空气污染指数：气象条件有利于空气污染物稀释、扩散和清除，可在室外正常活动。 紫外线指数：紫外线辐射强，建议涂擦SPF20左右、PA++的防晒护肤品。避免在10点至14点暴露于日光下。
						</string>
						<string>4月30日 晴</string>
						<string>11℃/27℃</string>
						<string>北风3-4级转无持续风向微风</string>
						<string>0.gif</string>
						<string>0.gif</string>
						<string>5月1日 晴转多云</string>
						<string>12℃/25℃</string>
						<string>无持续风向微风</string>
						<string>0.gif</string>
						<string>1.gif</string>
						<string>5月2日 多云转晴</string>
						<string>13℃/26℃</string>
						<string>无持续风向微风</string>
						<string>1.gif</string>
						<string>0.gif</string>
						<string>5月3日 多云转阴</string>
						<string>11℃/23℃</string>
						<string>无持续风向微风</string>
						<string>1.gif</string>
						<string>2.gif</string>
						<string>5月4日 阴转多云</string>
						<string>14℃/27℃</string>
						<string>无持续风向微风</string>
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
		// 设置与.Net提供的Web Service保持较好的兼容性
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
