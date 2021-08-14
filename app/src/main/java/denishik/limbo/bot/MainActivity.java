package denishik.limbo.bot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.util.*;

import java.io.ByteArrayOutputStream;
import java.util.*;

import android.widget.LinearLayout;

import de.hdodenhof.circleimageview.*;

import android.widget.ImageView;
import android.app.Activity;
import android.content.SharedPreferences;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


public class MainActivity extends AppCompatActivity {


    private LinearLayout linear1;
    private CircleImageView logoApp;
    private ImageView athor;

    private SharedPreferences file;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.main);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        linear1 = (LinearLayout) findViewById(R.id.linear1);
        logoApp = (CircleImageView) findViewById(R.id.logoApp);
        athor = (ImageView) findViewById(R.id.athor);
        file = getSharedPreferences("filr", Activity.MODE_PRIVATE);
    }

    private void initializeLogic() {
        if (file.getString("login", "").length() > 5) {
            _loginSSH(file.getString("login", ""), file.getString("pass", ""));
        } else {
            _loginView();
        }
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {

        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {

            default:
                break;
        }
    }

    public void _loginView() {
        final com.google.android.material.bottomsheet.BottomSheetDialog dialog = new com.google.android.material.bottomsheet.BottomSheetDialog(MainActivity.this);
        View lay = getLayoutInflater().inflate(R.layout.login_viee, null);
        dialog.setContentView(lay);
        final LinearLayout linear1 = (LinearLayout) lay.findViewById(R.id.linear1);
        final LinearLayout go = (LinearLayout) lay.findViewById(R.id.go);

        final EditText login = lay.findViewById(R.id.login);
        final EditText password = lay.findViewById(R.id.password);


        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();
        android.graphics.drawable.GradientDrawable wd = new android.graphics.drawable.GradientDrawable();
        wd.setColor(Color.WHITE);
        wd.setCornerRadius((int) 10f);
        linear1.setBackground(wd);
        if ((5 < login.getText().toString().length()) && (5 < password.getText().toString().length())) {
            file.edit().putString("login", login.getText().toString()).commit();
            file.edit().putString("pass", password.getText().toString()).commit();
            _loginSSH(login.getText().toString(), password.getText().toString());

        } else {
            OtherUtil.CustomToast(getApplicationContext(), "Чтото не так", 0xFFF44336, 10, Color.TRANSPARENT, 20, OtherUtil.BOTTOM);
        }
    }


    public void _loginSSH(final String _login, final String _pass) {
        try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(_login, "https://denishik.ru", 22);
			session.setPassword(_pass);

			// Avoid asking for key confirmation
			Properties prop = new Properties();
			prop.put("StrictHostKeyChecking", "no");
			session.setConfig(prop);

			// connect to ssh
			session.connect();

			// SSH Channel
			ChannelExec channelssh = (ChannelExec)
					session.openChannel("exec");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			channelssh.setOutputStream(baos);

			// Crossing to HomeActivity
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setClass(getApplicationContext(), HomeActivity.class);
            startActivity(i);
			

			// Execute command
//			channelssh.setCommand("lsusb > /home/pi/test.txt");
//			channelssh.connect();
//			channelssh.disconnect();

        } catch (Exception e) {
            OtherUtil.CustomToast(getApplicationContext(), e.toString(),0xFFF44336, 10, Color.TRANSPARENT, 20, OtherUtil.BOTTOM);
        }
    }


    @Deprecated
    public void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

    @Deprecated
    public int getLocationX(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[0];
    }

    @Deprecated
    public int getLocationY(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[1];
    }

    @Deprecated
    public int getRandom(int _min, int _max) {
        Random random = new Random();
        return random.nextInt(_max - _min + 1) + _min;
    }

    @Deprecated
    public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
        ArrayList<Double> _result = new ArrayList<Double>();
        SparseBooleanArray _arr = _list.getCheckedItemPositions();
        for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
            if (_arr.valueAt(_iIdx))
                _result.add((double) _arr.keyAt(_iIdx));
        }
        return _result;
    }

    @Deprecated
    public float getDip(int _input) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
    }

    @Deprecated
    public int getDisplayWidthPixels() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Deprecated
    public int getDisplayHeightPixels() {
        return getResources().getDisplayMetrics().heightPixels;
    }

}
