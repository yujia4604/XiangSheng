package yujia.model;

import yujia.util.MyUtil;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

public abstract class BaseAsyncTask extends AsyncTask<Object, Object, Integer> {
	private ProgressDialog dialog;
	private Context context;

	public BaseAsyncTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPostExecute(Integer o) {
		baseOnPostExcute();
	}

	public void baseOnPostExcute() {
		if (dialog == null || !dialog.isShowing())
			return;
		dialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = ProgressDialog.show(context, "请稍候...", null);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				dialog = null;
				MyUtil.makeToast(context, "已取消", true);
			}
		});
		dialog.show();
	}

}
