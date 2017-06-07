package com.example.bpdiagnostics.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bpdiagnostics.OnFragmentSearchLisntener;
import com.example.bpdiagnostics.R;
import com.example.bpdiagnostics.helpers.PreferencesManager;
import com.example.bpdiagnostics.helpers.StatisticsHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;


public class UserStatisticsFragment extends Fragment {


    private OnFragmentSearchLisntener mListener;

    @BindView(R.id.text_state)
    TextView textState;
    @BindView(R.id.text_sistolic_var)
    TextView textSistolic;
    @BindView(R.id.text_diastolic_var)
    TextView textDiastolic;


    private Unbinder unbinder;


    private LineChartView lineChartView;
    private ColumnChartView columnChartView;

    private long id;

    private StatisticsHelper statisticsHelper;


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public static UserStatisticsFragment newInstance(long id) {
        UserStatisticsFragment fragment = new UserStatisticsFragment();
        Bundle args = new Bundle();
        args.putLong(PreferencesManager.KEY_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong(PreferencesManager.KEY_ID);
        }

        statisticsHelper = new StatisticsHelper(getContext(), id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_statistics, container, false);

        unbinder = ButterKnife.bind(this, v);


        lineChartView = (LineChartView) v.findViewById(R.id.chart_line);
        columnChartView = (ColumnChartView) v.findViewById(R.id.chart_column);

        setData();

        textState.setText("Стан здоровя: " + statisticsHelper.getState());

        textSistolic.setText("Нижня від "+statisticsHelper.getMinS()+" до " +statisticsHelper.getMaxS());
        textDiastolic.setText("Верхня від "+statisticsHelper.getMinD()+" до " +statisticsHelper.getMaxD());

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSearchLisntener) {
            mListener = (OnFragmentSearchLisntener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentSearchLisntener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void setData() {


        //In most cased you can call data model methods in builder-pattern-like manner.
        Line linenorma = new Line(statisticsHelper.getNorma()).setColor(Color.BLUE).setFilled(true).setHasPoints(false);
        Line linegiper = new Line(statisticsHelper.getGiper()).setColor(Color.RED).setFilled(true).setHasPoints(false);


        Line line1 = new Line(statisticsHelper.getEntry1()).setColor(Color.GREEN).setHasLines(false);
        Line line2 = new Line(statisticsHelper.getEntry2()).setColor(Color.YELLOW).setHasLines(false);
        Line line3 = new Line(statisticsHelper.getEntry3()).setColor(Color.RED).setHasLines(false);

        Line average = new Line(statisticsHelper.getAverage()).setColor(Color.BLACK).setHasLines(false).setPointRadius(10);
        Line last = new Line(statisticsHelper.getLast()).setColor(Color.CYAN).setHasLines(false).setPointRadius(10);

        Line lineAll = new Line(statisticsHelper.getEntryAll()).setColor(Color.GRAY).setFilled(true)
                .setPointColor(Color.GRAY).setPointRadius(3);


        List<Line> lines = new ArrayList<Line>();

        lines.add(linenorma);
        lines.add(linegiper);

        lines.add(line1);
        lines.add(line2);
        lines.add(line3);

        lines.add(average);
        lines.add(last);

        lines.add(lineAll);

        LineChartData data = new LineChartData();

        data.setLines(lines);

        lineChartView.setLineChartData(data);

        Axis axisX = new Axis();
        axisX.setName("Систолічний АТ");
        axisX.setHasSeparationLine(true);
        data.setAxisXBottom(axisX);

        Axis axisY = new Axis();
        axisY.setName("Діастолічний АТ");
        axisY.setHasSeparationLine(true);
        data.setAxisYLeft(axisY);


        ColumnChartData datacolumns = new ColumnChartData(statisticsHelper.getColumnValues());
        columnChartView.setColumnChartData(datacolumns);


        Axis axisYColumn = new Axis();
        axisYColumn.setName("Кількість спостережень");
        axisYColumn.setHasSeparationLine(true);
        datacolumns.setAxisYLeft(axisYColumn);

        axisYColumn.setHasTiltedLabels(true);

    }

}
