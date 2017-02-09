package ldemyanenko.com.testappddapp.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import ldemyanenko.com.testappddapp.MainActivity;
import ldemyanenko.com.testappddapp.R;


public class FilterDialog extends Dialog implements
        View.OnClickListener {

    private final MainActivity activity;
    public Button ok;
    private Button clear;
    private Spinner filterSpinner;
    private ArrayAdapter<String> adapter;
    private EditText filterMart;
    private TextInputLayout filterMarkLayout;

    public FilterDialog(MainActivity a) {
        super(a);
        this.activity = a;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.courses_dialog_title);
        setContentView(R.layout.filter_dialog);
        ok = (Button) findViewById(R.id.filter_button_ok);
        clear = (Button) findViewById(R.id.filter_button_clear);
        filterSpinner = (Spinner) findViewById(R.id.course_spinner);
        filterMart = (EditText) findViewById(R.id.filter_mark);
        filterMarkLayout = (TextInputLayout) findViewById(R.id.filter_mark_text_input);
        filterSpinner.setAdapter(adapter);
        ok.setOnClickListener(this);
        clear.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter_button_ok:
                String mark = filterMart.getText().toString();
                if(mark.equals("")){
                    filterMarkLayout.setError(getContext().getResources().getString(R.string.filter_mark_empty_error));
                }else {
                    activity.setFilter((String) filterSpinner.getSelectedItem(),Integer.valueOf(mark));
                    filterMarkLayout.setError(null);
                    dismiss();
                }
                break;
            case R.id.filter_button_clear:
                activity.clearFilter();
                filterMart.setText(null);
                dismiss();
                break;
            default:
                break;
        }
    }


    public void prepare(List<String> spinnerArray) {
        adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }


}