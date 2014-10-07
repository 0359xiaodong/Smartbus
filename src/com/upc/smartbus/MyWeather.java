package com.upc.smartbus;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MyWeather extends Activity
{
	private Spinner provinceSpinner;
	private Spinner citySpinner;
	private ImageView todayWhIcon1;
	private ImageView todayWhIcon2;
	private TextView textWeatherToday;
	private ImageView tomorrowWhIcon1;
	private ImageView tomorrowWhIcon2;
	private TextView textWeatherTomorrow;
	private ImageView afterdayWhIcon1;
	private ImageView afterdayWhIcon2;
	private TextView textWeatherAfterday;
	private TextView textWeatherCurrent;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getWindow().setBackgroundDrawableResource(R.drawable.weatherbk);
		todayWhIcon1 = (ImageView) findViewById(R.id.todayWhIcon1);
		todayWhIcon2 = (ImageView) findViewById(R.id.todayWhIcon2);
		textWeatherToday = (TextView) findViewById(R.id.weatherToday);
		tomorrowWhIcon1 = (ImageView) findViewById(R.id.tomorrowWhIcon1);
		tomorrowWhIcon2 = (ImageView) findViewById(R.id.tomorrowWhIcon2);
		textWeatherTomorrow = (TextView) findViewById(R.id.weatherTomorrow);
		afterdayWhIcon1 = (ImageView) findViewById(R.id.afterdayWhIcon1);
		afterdayWhIcon2 = (ImageView) findViewById(R.id.afterdayWhIcon2);
		textWeatherAfterday = (TextView) findViewById(R.id.weatherAfterday);
		textWeatherCurrent = (TextView) findViewById(R.id.weatherCurrent);

		// ��ȡ���������ѡ��ʡ�ݡ����е�Spinner���
		provinceSpinner = (Spinner) findViewById(R.id.province);
		citySpinner = (Spinner) findViewById(R.id.city);
		// ����Զ��Web Service��ȡʡ���б�
		List<String> provinces = WebServiceUtil.getProvinceList();
		ListAdapter adapter = new ListAdapter(this, provinces);
		// ʹ��Spinner��ʾʡ���б�
		provinceSpinner.setAdapter(adapter);
		// ��ʡ��Spinner��ѡ����ı�ʱ
		provinceSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> source, View parent,
					int position, long id)
			{
				// ����ʡ�ݻ�ȡ�����б�
				List<String> cities = WebServiceUtil
						.getCityListByProvince(provinceSpinner.getSelectedItem()
								.toString());
				ListAdapter cityAdapter = new ListAdapter(MyWeather.this,
						cities);
				// ʹ��Spinner��ʾ�����б�
				citySpinner.setAdapter(cityAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});
		// ������Spinner��ѡ����ı�ʱ
		citySpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> source, View parent,
					int position, long id)
			{
				//չ������Ԥ���ľ�������
				showWeather(citySpinner.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});
	}
	//չ������Ԥ���ľ�������
	private void showWeather(String city)
	{
		String weatherToday = null;
		String weatherTomorrow = null;
		String weatherAfterday = null;
		String weatherCurrent = null;
		int iconToday[] = new int[2];
		int iconTomorrow[] = new int[2];
		int iconAfterday[] = new int[2];
		// ��ȡԶ��Web Service���صĶ���
		SoapObject detail = WebServiceUtil.getWeatherByCity(city);// ���ݳ��л�ȡ���о����������
		// ��ȡ����ʵ��
		weatherCurrent = detail.getProperty(4).toString();
		// ����������������
		String date = detail.getProperty(7).toString();
		weatherToday = "���죺" + date.split(" ")[0];
		weatherToday = weatherToday + "\n������" + date.split(" ")[1];
		weatherToday = weatherToday + "\n���£�"
				+ detail.getProperty(8).toString();
		weatherToday = weatherToday + "\n������"
				+ detail.getProperty(9).toString() + "\n";
		iconToday[0] = parseIcon(detail.getProperty(10).toString());
		iconToday[1] = parseIcon(detail.getProperty(11).toString());
		// ����������������
		date = detail.getProperty(12).toString();
		weatherTomorrow = "���죺" + date.split(" ")[0];
		weatherTomorrow = weatherTomorrow + "\n������" + date.split(" ")[1];
		weatherTomorrow = weatherTomorrow + "\n���£�"
				+ detail.getProperty(13).toString();
		weatherTomorrow = weatherTomorrow + "\n������"
				+ detail.getProperty(14).toString() + "\n";
		iconTomorrow[0] = parseIcon(detail.getProperty(15).toString());
		iconTomorrow[1] = parseIcon(detail.getProperty(16).toString());
		// ����������������
		date = detail.getProperty(17).toString();
		weatherAfterday = "���죺" + date.split(" ")[0];
		weatherAfterday = weatherAfterday + "\n������" + date.split(" ")[1];
		weatherAfterday = weatherAfterday + "\n���£�"
				+ detail.getProperty(18).toString();
		weatherAfterday = weatherAfterday + "\n������"
				+ detail.getProperty(19).toString() + "\n";
		iconAfterday[0] = parseIcon(detail.getProperty(20).toString());
		iconAfterday[1] = parseIcon(detail.getProperty(21).toString());
		// ���µ��������ʵ��
		textWeatherCurrent.setText(weatherCurrent);
		// ������ʾ����������ͼ����ı���
		textWeatherToday.setText(weatherToday);
		todayWhIcon1.setImageResource(iconToday[0]);
		todayWhIcon2.setImageResource(iconToday[1]);
		// ������ʾ����������ͼ����ı���
		textWeatherTomorrow.setText(weatherTomorrow);
		tomorrowWhIcon1.setImageResource(iconTomorrow[0]);
		tomorrowWhIcon2.setImageResource(iconTomorrow[1]);
		// ������ʾ����������ͼ����ı���
		textWeatherAfterday.setText(weatherAfterday);
		afterdayWhIcon1.setImageResource(iconAfterday[0]);
		afterdayWhIcon2.setImageResource(iconAfterday[1]);
	}

	// ���߷������÷�������ѷ��ص�����ͼ���ַ�����ת��Ϊ�����ͼƬ��ԴID��
	private int parseIcon(String strIcon)
	{
		if (strIcon == null)
			return -1;
		if ("0.gif".equals(strIcon))
			return R.drawable.a_0;
		if ("1.gif".equals(strIcon))
			return R.drawable.a_1;
		if ("2.gif".equals(strIcon))
			return R.drawable.a_2;
		if ("3.gif".equals(strIcon))
			return R.drawable.a_3;
		if ("4.gif".equals(strIcon))
			return R.drawable.a_4;
		if ("5.gif".equals(strIcon))
			return R.drawable.a_5;
		if ("6.gif".equals(strIcon))
			return R.drawable.a_6;
		if ("7.gif".equals(strIcon))
			return R.drawable.a_7;
		if ("8.gif".equals(strIcon))
			return R.drawable.a_8;
		if ("9.gif".equals(strIcon))
			return R.drawable.a_9;
		if ("10.gif".equals(strIcon))
			return R.drawable.a_10;
		if ("11.gif".equals(strIcon))
			return R.drawable.a_11;
		if ("12.gif".equals(strIcon))
			return R.drawable.a_12;
		if ("13.gif".equals(strIcon))
			return R.drawable.a_13;
		if ("14.gif".equals(strIcon))
			return R.drawable.a_14;
		if ("15.gif".equals(strIcon))
			return R.drawable.a_15;
		if ("16.gif".equals(strIcon))
			return R.drawable.a_16;
		if ("17.gif".equals(strIcon))
			return R.drawable.a_17;
		if ("18.gif".equals(strIcon))
			return R.drawable.a_18;
		if ("19.gif".equals(strIcon))
			return R.drawable.a_19;
		if ("20.gif".equals(strIcon))
			return R.drawable.a_20;
		if ("21.gif".equals(strIcon))
			return R.drawable.a_21;
		if ("22.gif".equals(strIcon))
			return R.drawable.a_22;
		if ("23.gif".equals(strIcon))
			return R.drawable.a_23;
		if ("24.gif".equals(strIcon))
			return R.drawable.a_24;
		if ("25.gif".equals(strIcon))
			return R.drawable.a_25;
		if ("26.gif".equals(strIcon))
			return R.drawable.a_26;
		if ("27.gif".equals(strIcon))
			return R.drawable.a_27;
		if ("28.gif".equals(strIcon))
			return R.drawable.a_28;
		if ("29.gif".equals(strIcon))
			return R.drawable.a_29;
		if ("30.gif".equals(strIcon))
			return R.drawable.a_30;
		if ("31.gif".equals(strIcon))
			return R.drawable.a_31;
		return 0;
	}
}