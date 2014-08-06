piano
=====

Android 钢琴组件
版本介绍
  第一版 
      目前适用于屏幕下方与左侧
      
使用方法
  1.在eclipse中使用addLibrary添加该库
  
  2.在xml文件中添加
  
    xmlns:piano="http://schemas.android.com/apk/res-auto"
    
  3.然后可以添加自定义组件了，如
  
    <com.jambla.piano.Piano
        android:id="@+id/line_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_alignParentBottom="true"
        android:clickable="true"
        android:focusable="true"
        android:gravity="center_horizontal"
        android:orientation="horizontal"
        piano:piano="@layout/hobby_btn"
        piano:piano_animation_duration="100"
        piano:piano_height="@dimen/btn_height"
        piano:piano_hidden="@dimen/btn_margin_bottom"
        piano:piano_margin="@dimen/btn_margin"
        piano:piano_num="7"
        piano:piano_width="@dimen/btn_width" >
    </com.jambla.piano.Piano>
  
