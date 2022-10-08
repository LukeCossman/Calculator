package com.hfad.calculator_lc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.lang.Math;

public class MainActivity extends AppCompatActivity {

    //Constants for referencing operator signs
    final static String TIMES = "X";
    final static String DIVIDE = "/";
    final static String PLUS = "+";
    final static String MINUS = "-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Array of user inputs
        ArrayList<String> userInput = new ArrayList<String>();

        //Misc buttons + the text display
        Button btnClear = findViewById(R.id.btn_clear);
        Button btnSign = findViewById(R.id.btn_sign);
        Button btnPercent = findViewById(R.id.btn_percent);
        Button btnDecimal = findViewById(R.id.btn_decimal);
        Button btnEquals = findViewById(R.id.btn_equals);
        TextView txtOutput = findViewById(R.id.txt_output);

        //Operation buttons
        Button btnDivide = findViewById(R.id.btn_divide);
        Button btnMultiply = findViewById(R.id.btn_multiply);
        Button btnMinus = findViewById(R.id.btn_minus);
        Button btnPlus = findViewById(R.id.btn_plus);

        //Number buttons
        Button btnNine = findViewById(R.id.btn_nine);
        Button btnEight = findViewById(R.id.btn_eight);
        Button btnSeven = findViewById(R.id.btn_seven);
        Button btnSix = findViewById(R.id.btn_six);
        Button btnFive = findViewById(R.id.btn_five);
        Button btnFour = findViewById(R.id.btn_four);
        Button btnThree = findViewById(R.id.btn_three);
        Button btnTwo = findViewById(R.id.btn_two);
        Button btnOne = findViewById(R.id.btn_one);
        Button btnZero = findViewById(R.id.btn_zero);

        //Add logic for number buttons
        Button buttons[] = {btnNine, btnEight, btnSeven, btnSix, btnFive, btnFour, btnThree, btnTwo, btnOne, btnZero};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    //Get text from display and update it
                    String text = ((Button) view).getText().toString();
                    txtOutput.setText(updateScreen(txtOutput.getText().toString(), text, userInput));
                }
            });
        }

        //Add logic for operator buttons
        Button operands[] = {btnDivide, btnMultiply, btnMinus, btnPlus};
        for (int i = 0; i < operands.length; i++) {
            operands[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    //Get operator clicked and whatever number is in display
                    String operator = ((Button) view).getText().toString();
                    String text = txtOutput.getText().toString();

                    //If the display is an ERROR, change it to 0
                    if (text.equals("ERROR!")) {
                        text = "0";
                        txtOutput.setText(text);
                    }

                    //Add the number and operator to the arraylist
                    userInput.add(text);
                    userInput.add(operator);
                    System.out.println(userInput);
                }
            });
        }

        //Add logic to decimal button
        btnDecimal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //Add decimal only if decimal not already present
                String text = txtOutput.getText().toString();
                if (!text.contains(".")) {
                    text.concat(".");
                }
            }
        });

        //Add logic to sign change button
        btnSign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //Remove minus sign if already present, else add it to
                //the front of the display
                String text = txtOutput.getText().toString();
                if (text.substring(0, 1).equals(MINUS)) {
                    text = text.substring(1);
                } else {
                    text = MINUS.concat(text);
                }
                txtOutput.setText(text);
            }
        });

        //Add logic to percent button
        btnPercent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //Get display, turn it into number, divide it by 100
                String text = txtOutput.getText().toString();
                double num = Double.parseDouble(text);
                num = num / 100;
                text = Double.toString(num);
                txtOutput.setText(text);
            }
        });

        //Add logic to clear button
        btnClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //Clear arraylist, return display to default value (aka 0)
                txtOutput.setText("0");
                userInput.clear();
            }
        });

        //Add logic to equals button
        btnEquals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //Set up variables
                String text = txtOutput.getText().toString(); //current display
                userInput.add(text); //add display to arraylist
                int chosen; //index of first operator
                double num; //value of two operated integers (ie 5 if array has 3, +, 2)
                ArrayList<String> mainList = copy(userInput); //copy of arraylist so it can be manipulated
                userInput.clear();
                ArrayList<String> temp; //temporary arraylist to be added to

                /** REDUCE MULTIPLY OR DIVIDE **/
                while (retrieve(mainList, TIMES, DIVIDE) != -1) {
                    temp = new ArrayList<String>(); //create empty array list
                    chosen = retrieve(mainList, TIMES, DIVIDE); //find first mult or divide operator

                    //For each item in array list...
                    for (int i = 0; i < mainList.size(); i++) {
                        //...if it is not next to the chosen operator, add it to the temp array list
                        if (i < chosen - 1 || i > chosen + 1) {
                            temp.add(mainList.get(i));
                        //...if it IS the chosen operator, do the operation...
                        } else if (i == chosen) {
                            //multiply
                            if (mainList.get(chosen).equals(TIMES)) {
                                num = Double.parseDouble(mainList.get(chosen - 1)) *
                                        Double.parseDouble(mainList.get(chosen + 1));
                            //divide
                            } else {
                                num = Double.parseDouble(mainList.get(chosen - 1)) /
                                        Double.parseDouble(mainList.get(chosen + 1));
                            }
                            temp.add(Double.toString(num)); //add value to temp array list

                        }
                    }
                    mainList = temp; //set mainList to newly shortened array list so previous process can repeat
                }
                /** REDUCE ADD OR SUBTRACT **/
                while (retrieve(mainList, PLUS, MINUS) != -1) {
                    temp = new ArrayList<String>(); //create empty array list
                    chosen = retrieve(mainList, PLUS, MINUS); //find first add or subtract operator

                    //For each item in array list...
                    for (int i = 0; i < mainList.size(); i++) {
                        //...if it is not next to the chosen operator, add it to the temp array list
                        if (i < chosen - 1 || i > chosen + 1) {
                            temp.add(mainList.get(i));
                        //...if it IS the chosen operator, do the operation...
                        } else if (i == chosen) {
                            //addition
                            if (mainList.get(chosen).equals(PLUS)) {
                                num = Double.parseDouble(mainList.get(chosen - 1)) +
                                        Double.parseDouble(mainList.get(chosen + 1));
                            //subtraction
                            } else {
                                num = Double.parseDouble(mainList.get(chosen - 1)) -
                                        Double.parseDouble(mainList.get(chosen + 1));
                            }
                            temp.add(Double.toString(num)); //add value to temp array list
                        }
                    }
                    mainList = temp; //set mainList to newly shortened array list so previous process can repeat
                }
                //By this point the array list should be reduced to a single value.
                //That single value is the final answer
                String answer = mainList.get(0);
                //If divided by zero, throw and error
                if (answer.equals("Infinity")) {
                    txtOutput.setText("ERROR!");
                } else {
                    txtOutput.setText(answer);
                }
            }
        });

    }


    //----------------//
    //HELPER FUNCTIONS//
    //----------------//

    /** isOperator
     * This function determines whether the given string is one of the four operators
     * @param sign - String to test
     * @return true if operator, false otherwise
     */
    protected boolean isOperator(String sign)
    {
        String operators[] = {"+", "-", "/", "X"}; //list of operators
        boolean found = false; //boolean assumed false by default
        for (String s : operators)
        {
            //If string is same as operator, boolean set to true
            if (sign.equals(s))
            {
                found = true;
            }
        }
        return found; //return boolean
    }

    /** updateScreen
     * This function updates the display textview at the top of the screen
     * @param msg - The message that is already being displayed
     * @param s - The value that will add to or replace the current message
     * @param userInput - The arraylist of values
     * @return The updated message, whether it was concatenated or replaced
     */
    protected String updateScreen(String msg, String s, ArrayList<String> userInput)
    {
        boolean hasOp = false; //Assume operator did not just occur
        try {
            hasOp = isOperator(userInput.get(userInput.size()-1)); //Try to see if operator just occured
        } catch (Exception e) {}

        //If operator was just entered, user must be entering a new number,
        //therefore replace current display
        if (hasOp)
        {
            msg = s;
        }
        //If current display is default value (aka 0 or Error), replace current display
        else if (msg.equals("0") || msg.equals("ERROR!"))
        {
            msg = s;
        }
        //Else, user must be adding a digit to a number, therefore tack it on to the end
        //of the current display
        else
        {
            msg = msg + s;
        }
        return msg; //return new display
    }

    /** retrieve
     * Find the first of two given operators in an arraylist
     * @param list - arraylist to be scanned
     * @param op1 - first operator to find
     * @param op2 - second operator to find
     * @return index of first found operator, or -1 if no operators found
     */
    protected int retrieve(ArrayList<String> list, String op1, String op2)
    {
        for (String s : list)
        {
            if (s.equals(op1) || s.equals(op2))
            {
                return list.indexOf(s);
            }
        }
        return -1;
    }

    /** copy
     * Create a copy of an arraylist
     * @param original - arraylist to be copied
     * @return copy of original arraylist
     */
    protected ArrayList<String> copy(ArrayList<String> original)
    {
        ArrayList<String> clone = new ArrayList<String>();
        for (String s : original)
        {
            clone.add(s);
        }
        return clone;
    }
}