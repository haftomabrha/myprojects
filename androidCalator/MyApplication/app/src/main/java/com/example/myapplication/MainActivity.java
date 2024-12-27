package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;
    private StringBuilder expression;
    private String currentInput = "";
    private boolean isResultDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.input2); // Replace 'input2' with your TextView ID
        expression = new StringBuilder();
        textViewResult.setText(expression.toString());
        setupButtons();
    }

    private void setupButtons() {
        int[] buttonIds = {
                R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6,
                R.id.button7, R.id.button8, R.id.button9,
                R.id.button0, R.id.buttonadd, R.id.buttonsubt,
                R.id.buttonmulti, R.id.buttondiv, R.id.buttonsqaure,
                R.id.buttonequal, R.id.buttonclear, R.id.buttoncmodule,
                R.id.buttonDelete, R.id.buttonsquareRoot, R.id.setting, R.id.me,
                R.id.buttondot, R.id.buttonabsolute
        };

        for (int id : buttonIds) {
            Button button = findViewById(id);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int viewId = v.getId();
                    if (viewId == R.id.setting) {
                        openSettings();
                    } else if (viewId == R.id.me) {
                        showAboutDeveloper();
                    } else {
                        String value = ((Button) v).getText().toString();
                        handleButtonClick(value);
                    }
                }
            });
        }
    }

    private void handleButtonClick(String value) {
        try {
            switch (value) {
                case "=":
                    calculateResult();
                    break;
                case ".":
                    if (!currentInput.contains(".")) appendToInput(".");
                    break;
                case "C":
                    clear();
                    break;
                case "|x|":
                    calculateAbsoluteValue();
                    break;
                case "+": case "-": case "*": case "÷": case "%":
                    setOperator(value);
                    break;
                case "√":
                    calculateSquareRoot();
                    break;
                case "x²":


                    calculateSquare();
                    break;
                case "⌫":
                    handleDelete();
                    break;
                default:
                    appendToInput(value);
                    break;
            }
        } catch (Exception e) {
            displayError("Invalid Input");
        }
    }

    private void appendToInput(String value) {
        if (isResultDisplayed) clear();
        currentInput += value;
        addToExpression(value);
    }

    private void addToExpression(String value) {
        expression.append(value);
        textViewResult.setText(expression.toString());
    }

    private void setOperator(String value) {
        if (isResultDisplayed) {
            isResultDisplayed = false; // Reset result flag
            addToExpression(" " + value + " ");
            currentInput = ""; // Clear current input so further typing works normally
        } else if (currentInput.isEmpty() && value.equals("-")) {
            appendToInput(value); // Allow negative sign
        } else if (!currentInput.isEmpty()) {
            addToExpression(" " + value + " ");
            currentInput = "";
        }
    }


    private void calculateResult() {
        try {
            if (!expression.toString().isEmpty()) {
                double result = evaluateExpression(expression.toString());
                displayResult(result);
            }
        } catch (Exception e) {
            displayError("Invalid Input");
        }
    }

    private double evaluateExpression(String expr) {
        // Expression evaluation logic
        try {
            // Parse tokens and evaluate expression
            Stack<Double> values = new Stack<>();
            Stack<String> operators = new Stack<>();
            String[] tokens = expr.split(" ");

            for (String token : tokens) {
                if (token.matches("-?\\d+(\\.\\d+)?")) {
                    values.push(Double.parseDouble(token));
                } else if (isOperator(token)) {
                    while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                        performOperation(values, operators.pop());
                    }
                    operators.push(token);
                }
            }

            while (!operators.isEmpty()) {
                performOperation(values, operators.pop());
            }

            return values.pop();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Expression");
        }
    }

    private void performOperation(Stack<Double> values, String operator) {
        double secondOperand = values.pop();
        double firstOperand = values.pop();
        double result = 0;

        switch (operator) {
            case "+":
                result = firstOperand + secondOperand;
                break;
            case "-":
                result = firstOperand - secondOperand;
                break;
            case "*":
                result = firstOperand * secondOperand;
                break;
            case "÷":
                if (secondOperand == 0) throw new ArithmeticException("Division by Zero");
                result = firstOperand / secondOperand;
                break;
            case "%":
                result = firstOperand % secondOperand;
                break;
        }

        values.push(result);
    }

    private boolean isOperator(String token) {
        return token.matches("[+\\-*÷%]");
    }

    private int precedence(String operator) {
        switch (operator) {
            case "*": case "÷": case "%": return 2;
            case "+": case "-": return 1;
            default: return 0;
        }
    }

    private void calculateSquareRoot() {
        try {
            if (!currentInput.isEmpty()) {
                double value = Double.parseDouble(currentInput);
                if (value >= 0) {
                    double result = Math.sqrt(value);
                    textViewResult.setText("√" + currentInput + " = " + result);
                    expression.setLength(0);
                    expression.append(result);
                    currentInput = String.valueOf(result);
                    isResultDisplayed = true;
                } else {
                    textViewResult.setText("Invalid Input: Negative Input");
                }
            }
        } catch (NumberFormatException e) {
            textViewResult.setText("Invalid Input");
        }
    }

    private void calculateSquare() {
        try {
            if (!currentInput.isEmpty()) {
                double value = Double.parseDouble(currentInput);
                double result = value * value;
                textViewResult.setText(currentInput + "² = " + result);
                expression.setLength(0);
                expression.append(result);
                currentInput = String.valueOf(result);
                isResultDisplayed = true;
            }
        } catch (NumberFormatException e) {
            textViewResult.setText("Invalid Input");
        }
    }

    private void calculateAbsoluteValue() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                double result = Math.abs(value);
                textViewResult.setText("|" + currentInput + "| = " + result);
                expression.setLength(0);
                expression.append(result);
                currentInput = String.valueOf(result);
                isResultDisplayed = true;
            } catch (NumberFormatException e) {
                textViewResult.setText("Invalid Input");
            }
        }
    }

    private void displayResult(double result) {
        textViewResult.setText(String.valueOf(result));
        expression.setLength(0);
        expression.append(result);
        currentInput = String.valueOf(result);
        isResultDisplayed = true;
    }

    private void displayError(String message) {
        textViewResult.setText("invalid!");




    }

    private void clear() {
        currentInput = "";
        expression.setLength(0);
        textViewResult.setText("");
        isResultDisplayed = false;
    }

    private void handleDelete() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            expression.setLength(expression.length() - 1);
            textViewResult.setText(expression.toString());
        }
    }

    private void openSettings() {

        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void showAboutDeveloper() {
        startActivity(new Intent(this, AboutDeveloperActivity.class));
    }
}
