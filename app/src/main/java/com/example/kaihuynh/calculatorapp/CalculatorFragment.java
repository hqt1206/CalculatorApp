package com.example.kaihuynh.calculatorapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

import static android.content.Context.MODE_PRIVATE;

public class CalculatorFragment extends Fragment implements View.OnClickListener {

    private static final String PREF_CALCULATOR = "PREF_CALCULATOR";
    private static final String PREF_RESULT_KEY = "PREF_RESULT_KEY";
    private final char ADDITION = '+';
    private final char SUBTRACTION = '-';
    private final char MULTIPLICATION = 'x';
    private final char DIVISION = '/';
    private final String EQUAL_SYMBOL = "= ";
    private TextView mHistoryText;
    private TextView mExpressionText;
    private TextView mResultText;
    private double mFirstOperand;
    private double mSecondOperand;
    private char mOperator;
    private boolean isCalculated;
    private double mLastResult;

    public CalculatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        initComponents(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_number_zero:
                onOperandClick(getResources().getString(R.string.title_number_zero));
                break;
            case R.id.tv_number_one:
                onOperandClick(getResources().getString(R.string.title_number_one));
                break;
            case R.id.tv_number_two:
                onOperandClick(getResources().getString(R.string.title_number_two));
                break;
            case R.id.tv_number_three:
                onOperandClick(getResources().getString(R.string.title_number_three));
                break;
            case R.id.tv_number_four:
                onOperandClick(getResources().getString(R.string.title_number_four));
                break;
            case R.id.tv_number_five:
                onOperandClick(getResources().getString(R.string.title_number_five));
                break;
            case R.id.tv_number_six:
                onOperandClick(getResources().getString(R.string.title_number_six));
                break;
            case R.id.tv_number_seven:
                onOperandClick(getResources().getString(R.string.title_number_seven));
                break;
            case R.id.tv_number_eight:
                onOperandClick(getResources().getString(R.string.title_number_eight));
                break;
            case R.id.tv_number_nine:
                onOperandClick(getResources().getString(R.string.title_number_nine));
                break;
            case R.id.tv_all_clear:
                isCalculated = false;
                clearText();
                break;
            case R.id.tv_division:
                onOperatorClick(DIVISION);
                break;
            case R.id.tv_multiplication:
                onOperatorClick(MULTIPLICATION);
                break;
            case R.id.tv_addition:
                onOperatorClick(ADDITION);
                break;
            case R.id.tv_subtraction:
                onOperatorClick(SUBTRACTION);
                break;
            case R.id.tv_dot_symbol:
                if (mExpressionText.getText().toString().contains(".")) {
                    return;
                }
                if (mExpressionText.getText().toString().length() == 0) {
                    mExpressionText.setText("0.");
                } else {
                    if (isCalculated) {
                        mHistoryText.append(String.valueOf(
                                "\n" + mExpressionText.getText().toString()
                                        + "\n" + mResultText.getText()
                                        + "\n" + getResources().getString(R.string.line)));
                        mExpressionText.setText("0.");
                        mResultText.setText(getResources().getText(R.string.default_result));
                        isCalculated = false;
                    } else {
                        mExpressionText.append(".");
                    }
                }
                break;
            case R.id.tv_equal_symbol:
                if (!isContainOperator(mExpressionText.getText().toString())) {
                    if (!mExpressionText.getText().toString().equals("")) {
                        mResultText.setText(mExpressionText.getText());
                    }
                } else if (!isCalculated) {
                    try {
                        mSecondOperand = Double.parseDouble(mExpressionText.getText().toString().substring(2));
                    } catch (NumberFormatException e) {
                        mSecondOperand = 0;
                    }
                    mLastResult = calculate(mFirstOperand, mSecondOperand, mOperator);
                    mResultText.setText(String.valueOf(EQUAL_SYMBOL + formatNumber(mLastResult)));
                    mFirstOperand = mLastResult;
                    isCalculated = true;
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main, menu);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                clearText();
                return true;
            case R.id.action_save:
                SharedPreferences sharedPreferences =
                        getActivity().getSharedPreferences(PREF_CALCULATOR, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(PREF_RESULT_KEY, (float) mLastResult);
                editor.apply();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(PREF_CALCULATOR, MODE_PRIVATE);
        mFirstOperand = sharedPreferences.getFloat(PREF_RESULT_KEY, 0);
        mResultText.setText(String.valueOf(formatNumber(mFirstOperand)));

    }

    private void initComponents(View view) {
        view.findViewById(R.id.tv_number_zero).setOnClickListener(this);
        view.findViewById(R.id.tv_number_one).setOnClickListener(this);
        view.findViewById(R.id.tv_number_two).setOnClickListener(this);
        view.findViewById(R.id.tv_number_three).setOnClickListener(this);
        view.findViewById(R.id.tv_number_four).setOnClickListener(this);
        view.findViewById(R.id.tv_number_five).setOnClickListener(this);
        view.findViewById(R.id.tv_number_six).setOnClickListener(this);
        view.findViewById(R.id.tv_number_seven).setOnClickListener(this);
        view.findViewById(R.id.tv_number_eight).setOnClickListener(this);
        view.findViewById(R.id.tv_number_nine).setOnClickListener(this);
        view.findViewById(R.id.tv_all_clear).setOnClickListener(this);
        view.findViewById(R.id.tv_division).setOnClickListener(this);
        view.findViewById(R.id.tv_multiplication).setOnClickListener(this);
        view.findViewById(R.id.tv_addition).setOnClickListener(this);
        view.findViewById(R.id.tv_subtraction).setOnClickListener(this);
        view.findViewById(R.id.tv_dot_symbol).setOnClickListener(this);
        view.findViewById(R.id.tv_equal_symbol).setOnClickListener(this);
        view.findViewById(R.id.tv_percentage).setOnClickListener(this);
        mHistoryText = view.findViewById(R.id.tv_history);
        mExpressionText = view.findViewById(R.id.tv_expression);
        mResultText = view.findViewById(R.id.tv_result);

        mHistoryText.setMovementMethod(new ScrollingMovementMethod());
    }

    private void onOperandClick(String text) {
        if (isCalculated) {
            mHistoryText.append(String.valueOf(
                    "\n" + mExpressionText.getText().toString()
                            + "\n" + mResultText.getText()
                            + "\n" + getResources().getString(R.string.line)));
            mExpressionText.setText(text);
            mResultText.setText(getResources().getText(R.string.default_result));
            isCalculated = false;
        } else {
            mExpressionText.append(text);
        }
    }

    private void onOperatorClick(char symbol) {
        String expression2 = mExpressionText.getText().toString();
        if (isContainOperator(expression2)) {
            if (expression2.trim().length() > 1 && !isCalculated) {
                mSecondOperand = Double.parseDouble(expression2.substring(2));
                mLastResult = calculate(mFirstOperand, mSecondOperand, mOperator);
                mResultText.setText(String.valueOf(EQUAL_SYMBOL + formatNumber(mLastResult)));
                mHistoryText.append("\n" + expression2);
                mFirstOperand = mLastResult;
            } else if (expression2.trim().length() > 1 && isCalculated) {
                mHistoryText.append("\n" + expression2);
                isCalculated = false;
            } else {
                isCalculated = false;
            }
        } else {
            if (expression2.length() != 0) {
                mHistoryText.append("\n" + expression2);
                mFirstOperand = Double.parseDouble(expression2);
            } else {
                mHistoryText.append("\n" + mFirstOperand);
            }
        }
        mExpressionText.setText(String.valueOf(symbol + " "));
        mOperator = symbol;
    }

    private void clearText() {
        mHistoryText.setText("");
        mExpressionText.setText("");
        mResultText.setText("0");
    }

    private boolean isContainOperator(String text) {
        return text.contains("+") || text.contains("-") || text.contains("x") || text.contains("/");
    }

    private double calculate(double a, double b, char operator) {
        switch (operator) {
            case ADDITION:
                return a + b;
            case SUBTRACTION:
                return a - b;
            case MULTIPLICATION:
                return a * b;
            case DIVISION:
                return a / b;
        }
        return a;
    }

    private String formatNumber(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.########");
        return decimalFormat.format(number);
    }

}
