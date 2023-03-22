# Sry for readme, its for my mark



**114 Часть II. Базовое программирование для Android**

Прежде чем приступать к написанию Java-кода, нужно разобраться, как будет работать наше приложение. Максимальное значение для `ProgressBar` устанавливается в поле `мах value`, шаг (значение, на которое будет увеличено значение `ProgressBar` каждые полсекунды или 500 мс) задается значением, указанным в поле `increment by`. При нажатии на кнопку `start` появится диалоговое окно, в котором и будет отображен наш индикатор `ProgressBar`. Увеличение значения `ProgressBar` будет производиться в потоке каждые 500 мс. Понимаю, что с диалоговыми окнами мы еще пока не работали, но они будут рассмотрены уже в следующей главе. 
Полный исходный Java-код представлен в листинге 4.16. Внимательно читайте комментарии, чтобы разобраться что и к чему. На этот раз я не выделял никакие строки, поскольку выделять пришлось бы большую часть Java-кода. Также удалены обработчики меню, которые были в исходном проекте, — они только занимали место и не делали ничего полезного. 

> Листинг 4.16. Полный Java-код приложения (progressBar) package
```Java
com.example.ch04;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget,Button;
import android.widget.EditText;
import android,app.ProgressDialog;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity { 

	int increment;
	ProgressDialog dialog;
	
	©Override 
	protected void onCreate(Bundle savedlnstanceState) {
		super.onCreate(savedlnstanceState);
		setContentView(R.layout.activity_main);
	
		Button startbtn = (Button) findViewByld(R.id.startbtn);
		startbtn.setOnClickListener(new View.OnClickListener() { 
			public void onClick(View view) { 
			
				// получаем шаг инкремента из текстового поля 
				EditText et = (EditText) findViewByld(R.id.increment);
				// конвертируем строку в число 
				increment = Integer.parselnt(et.getText().toString());
				
				// создаем новый диалог 
				dialog = new ProgressDialog(MainActivity.this);
				dialog.setCancelable(true);
				dialog.setMessage("Загрузка...");
				// шкала должна быть горизонтальной
				dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				// значение шкалы по умолчанию — О
				dialog.setProgress(0);
				// получаем максимальное значение
				EditText шах = (EditText) findViewByld(R.id.maximum);
				// конвертируем строку в число
				int maximum = Integer.parselnt(max.getText().toString());
				// устанавливаем максимальное значение
				dialog.setMax(maximum);
				// отображаем диалог
				dialog.show(); *
				// создаем поток для обновления шкалы
				Thread background = new Thread(new Runnable() (
					public void run() {
					try {
					// увеличиваем значение шкалы каждые 500 мс,
					// пока не будет достигнуто максимальное значение
						while (dialog.getProgress() <= dialog.getMaxO) {
							// ждем 500 мс
							Thread.sleep(500);
							// активируем обработчик обновления
							progressHandler.sendMessage(progressHandler.obtainMessage());
					}
					}catch (java.lang.InterruptedExeception e) {
				}
			});
			// запускаем фоновый поток
			background.start();

		}
		// обработчик для фонового обноапения
		Handler progressHandler = new Handler О {
			public void handleMessage(Message msg) {
				// увеличиваем значение шкалы
				dialog.incrementProgressBy(increment);
			}
		};
	});
}
```

**116 Часть II. Базовое программирование для Android**
Запустите приложение, установите параметры (рис. 4.23) и нажмите кнопку ПУСК — вы увидите диалоговое окно и работающий в нем индикатор `ProgressBar` (рис. 4.24).

## <img alt="image_1.jpg" height="65" src="img_1.png" width="65"/>

## 4.2.4. Средства отображения графики
Для отображения графики служит виджет `imageview`, являющийся базовым элементом для графического наполнения.
Для загрузки изображения в классе `imageview` существует несколько методов:
setimageR

 - `set ImageResource (int resId)`— загружает изображение из ресурса; 
 - `set ImageURi (Uri uri)` — загружает изображение по его URI; 
 - `set ImageBitmap (Bitmap bitmap)` — загружает растровое изображение
 
**Глава 4. Интерфейс пользователя 117**
Для изменения размера изображения используются следующие методы:
 - `setMaxHeight ()` — устанавливает максимальную высоту; 
 - `setMaxWidth ()` — устанавливает максимальную ширину

Если вы хотите загрузить изображение из файла разметки, то используйте атрибут `android:src`. При добавлении imageview с помощью редактора разметки откроется окно, позволяющее выбрать изображение из списка ресурсов проекта или системных ресурсов. В файле разметки виджет `ImageView` определяется так:
```Xml
<ImageView android:layout_height="wrap_content"
android:id="@+id/imageViewl"
android:src="0drawable/icon"
android: layout_width="wrap_content">
</ImageView>
```

После этого в Java-коде работа с виджетом производится так:
```Java
final Imageview image = (Imageview)findViewById(R.id.imageViewl);
// загружаем изображение из ресурса
image.setImageResource(R .drawable.icon);
</ImageView>
```

В этой главе мы познакомились с основными виджетами. А об остальных виджетах вы можете прочитать в руководстве разработчика Android: http://developer.android.com/reference/android/package-summary.html. 
В следующей же главе мы поговорим об уведомлениях, диалоговых окнах и меню.

# ГЛАВА 5
## Уведомления, диалоговые окна и меню

## 5.1. Уведомления

## 5.1.1. Простое всплывающее уведомление
Приложения могут отображать два типа уведомлений: краткие всплывающие сообщения (Toast Notification) и постоянные напоминания (Status Ваг Notification). Первые отображаются на экране мобильного устройства какое-то время и не требуют внимания пользователя. Как правило, это не критические информационные сообщения. Вторые постоянно отображаются в строке состояния и требуют реакции пользователя. 
Например, приложение требует подключения к вашему серверу. Если соединение успешно установлено, можно отобразить краткое уведомление, а вот если подключиться не получилось, тогда отображается постоянное уведомление, чтобы пользователь сразу мог понять, почему приложение не работает. 
Чтобы отобразить всплывающее сообщение, используйте класс Toast и его методы `makeText` (создает текст уведомления) и `show` (отображает уведомление):
```Java
import android.widget.Toast; 
import android.content.Context; 
***
Context context = getApplicationContext(); 
Toast toast = Toast.makeText(context, "Это уведомление", Toast.LENGTH_LONG); 
toast.show();
```

Первый параметр метода `makeText()` — это контекст приложения, который можно получить с помощью вызова `getApplicationContext()`. Второй параметр— текст уведомления. Третий — задает продолжительность отображения уведомления:

 - `LENGHT_SHORT`— небольшая продолжительность (1-2 секунды)
   отображения текстового уведомления; 
  - `LENGHT_LONG`— показывает
   уведомление в течение более длительного периода времени (примерно 4
   секунды). Предпочтительнее использовать «длинные» уве­домления, поскольку пользователь может просто не успеть прочитать текст, выводимый программой.
   
**Глава 5. Уведомления, диалоговые окна и меню 119**
По умолчанию всплывающее уведомление появится в нижней части экрана. Чтобы отобразить уведомление в другом месте, можно воспользоваться методом
`setGravity()`, который нужно вызвать до метода `show()`: `toast.setGravity(Gravity.CENTER, 0, 0) ;`
Первый параметр задает размещение в пределах большего контейнера — например,
`GRAVIRY.CENTER, GREVITY.TOP` и т. д. Второй параметр — это смещение по оси X, третий — смещение по оси Y.
В нашем примере уведомление будет отображено по центру окна.
Теперь немного практики. Создайте новый проект (пусть он называется `Test6` —
для совместимости с моим кодом), разметку можете не изменять, а можете вообще
удалить все элементы деятельности.
Java-код приложения представлен в листинге 5.1. Это приложение отобразит при
запуске уведомление (рис. 5.1).
# Картинка
**120 Часть И. Базовое программирование для Android**

> Листинг 5.1. Отображение всплывающего уведомления
```Java
package com.example.ch05;

import android.support.v 7 .app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Context;

public class MainActivity extends AppCompatActivity (
	@Override
	protected void onCreate(Bundle savedlnstanceState) {
		super.onCreate(savedlnstanceState);
		setContentView(R.layout.activity_main);
		
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, "Это уведомление", Toast.LENGTH_LONG);
		toast.show();
	}
}
```

## 5.1.2. Уведомление в строке состояния
Создать уведомление в строке состояния немного сложнее. Начиная с API 16, вместо устаревшего метода `getNotification()` следует использовать метод `build()`. Интересно, что если взглянуть на исходный код Android, то видно, что старый метод вызывает метод `build()`. Видимо, разработчикам не понравилось имя метода, вот его и объявили устаревшим. 
Затем нужно сформировать уведомление с помощью специального менеджера. Ссылку на `NotificationManager` можно получить через вызов метода `getSystemService()`, передав ему в качестве параметра строковую константу `NOTIFICATION_SERVICE` , определенную в классе context. 
Выводится уведомление с помощью метода `notify()` — это своеобразный аналог метода `show()` о у параметра `Toast` из предыдущего примера (см. листинг 5.1). Взглянем на код с комментариями:
```Java
Notification.Builder builder = new Notification.Builder(context);

builder.setContentlntent(contentlntent)
				// маленькое изображение
		.setSmalllcon(R.drawable.small) /
				// большое изображение
		.setLargelcon(BitmapFactory.decodeResource(res, R.drawable.big))
```
