package com.example.geocalc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.Locale;

/**
 * Actividad principal de GeoCalc.
 * Muestra cuatro pestañas con TabHost para calcular áreas y perímetros.
 */
public class MainActivity extends Activity {

    // Campos y resultados para Cuadrado.
    private EditText squareSideInput;
    private TextView squareAreaText;
    private TextView squarePerimeterText;

    // Campos y resultados para Triángulo.
    private EditText triangleBaseInput;
    private EditText triangleHeightInput;
    private EditText triangleSide2Input;
    private EditText triangleSide3Input;
    private TextView triangleAreaText;
    private TextView trianglePerimeterText;

    // Campos y resultados para Círculo.
    private EditText circleRadiusInput;
    private TextView circleAreaText;
    private TextView circlePerimeterText;

    // Campos y resultados para Rectángulo.
    private EditText rectangleBaseInput;
    private EditText rectangleHeightInput;
    private TextView rectangleAreaText;
    private TextView rectanglePerimeterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureTabs();
        bindViews();
        configureButtons();
    }

    /**
     * Configura las pestañas del TabHost: Cuadrado, Triángulo, Círculo y Rectángulo.
     */
    private void configureTabs() {
        TabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();

        addTab(tabHost, "cuadrado", "Cuadrado", R.id.tab_square);
        addTab(tabHost, "triangulo", "Triángulo", R.id.tab_triangle);
        addTab(tabHost, "circulo", "Círculo", R.id.tab_circle);
        addTab(tabHost, "rectangulo", "Rectángulo", R.id.tab_rectangle);
    }

    /**
     * Agrega una pestaña al TabHost con su etiqueta y contenido asociado.
     */
    private void addTab(TabHost tabHost, String tag, String label, int contentId) {
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setIndicator(label);
        tabSpec.setContent(contentId);
        tabHost.addTab(tabSpec);
    }

    /**
     * Enlaza los componentes del layout XML con variables Java.
     */
    private void bindViews() {
        squareSideInput = findViewById(R.id.input_square_side);
        squareAreaText = findViewById(R.id.text_square_area);
        squarePerimeterText = findViewById(R.id.text_square_perimeter);

        triangleBaseInput = findViewById(R.id.input_triangle_base);
        triangleHeightInput = findViewById(R.id.input_triangle_height);
        triangleSide2Input = findViewById(R.id.input_triangle_side2);
        triangleSide3Input = findViewById(R.id.input_triangle_side3);
        triangleAreaText = findViewById(R.id.text_triangle_area);
        trianglePerimeterText = findViewById(R.id.text_triangle_perimeter);

        circleRadiusInput = findViewById(R.id.input_circle_radius);
        circleAreaText = findViewById(R.id.text_circle_area);
        circlePerimeterText = findViewById(R.id.text_circle_perimeter);

        rectangleBaseInput = findViewById(R.id.input_rectangle_base);
        rectangleHeightInput = findViewById(R.id.input_rectangle_height);
        rectangleAreaText = findViewById(R.id.text_rectangle_area);
        rectanglePerimeterText = findViewById(R.id.text_rectangle_perimeter);
    }

    /**
     * Asigna las acciones de cálculo a cada botón.
     */
    private void configureButtons() {
        Button squareButton = findViewById(R.id.button_square_calculate);
        Button triangleButton = findViewById(R.id.button_triangle_calculate);
        Button circleButton = findViewById(R.id.button_circle_calculate);
        Button rectangleButton = findViewById(R.id.button_rectangle_calculate);

        squareButton.setOnClickListener(view -> calculateSquare());
        triangleButton.setOnClickListener(view -> calculateTriangle());
        circleButton.setOnClickListener(view -> calculateCircle());
        rectangleButton.setOnClickListener(view -> calculateRectangle());
    }

    /**
     * Calcula Área = lado * lado y Perímetro = 4 * lado para el cuadrado.
     */
    private void calculateSquare() {
        Double side = getPositiveValue(squareSideInput, "Ingrese un lado válido");
        if (side == null) {
            return;
        }

        double area = side * side;
        double perimeter = 4 * side;
        showResults(squareAreaText, squarePerimeterText, area, perimeter);
    }

    /**
     * Calcula Área = (base * altura) / 2 y Perímetro = base + lado2 + lado3.
     */
    private void calculateTriangle() {
        Double base = getPositiveValue(triangleBaseInput, "Ingrese una base válida");
        Double height = getPositiveValue(triangleHeightInput, "Ingrese una altura válida");
        Double side2 = getPositiveValue(triangleSide2Input, "Ingrese el lado 2 válido");
        Double side3 = getPositiveValue(triangleSide3Input, "Ingrese el lado 3 válido");

        if (base == null || height == null || side2 == null || side3 == null) {
            return;
        }

        double area = (base * height) / 2;
        double perimeter = base + side2 + side3;
        showResults(triangleAreaText, trianglePerimeterText, area, perimeter);
    }

    /**
     * Calcula Área = π * radio² y Perímetro = 2 * π * radio para el círculo.
     */
    private void calculateCircle() {
        Double radius = getPositiveValue(circleRadiusInput, "Ingrese un radio válido");
        if (radius == null) {
            return;
        }

        double area = Math.PI * Math.pow(radius, 2);
        double perimeter = 2 * Math.PI * radius;
        showResults(circleAreaText, circlePerimeterText, area, perimeter);
    }

    /**
     * Calcula Área = base * altura y Perímetro = 2 * (base + altura).
     */
    private void calculateRectangle() {
        Double base = getPositiveValue(rectangleBaseInput, "Ingrese una base válida");
        Double height = getPositiveValue(rectangleHeightInput, "Ingrese una altura válida");

        if (base == null || height == null) {
            return;
        }

        double area = base * height;
        double perimeter = 2 * (base + height);
        showResults(rectangleAreaText, rectanglePerimeterText, area, perimeter);
    }

    /**
     * Valida que el campo no esté vacío y que el valor sea un número positivo.
     */
    private Double getPositiveValue(EditText editText, String errorMessage) {
        String valueText = editText.getText().toString().trim();

        if (valueText.isEmpty()) {
            editText.setError("Este campo es obligatorio");
            editText.requestFocus();
            return null;
        }

        try {
            double value = Double.parseDouble(valueText);
            if (value <= 0) {
                editText.setError("Ingrese un valor mayor que cero");
                editText.requestFocus();
                return null;
            }
            editText.setError(null);
            return value;
        } catch (NumberFormatException exception) {
            editText.setError(errorMessage);
            editText.requestFocus();
            return null;
        }
    }

    /**
     * Muestra los resultados con dos decimales.
     */
    private void showResults(TextView areaText, TextView perimeterText, double area, double perimeter) {
        areaText.setText(String.format(Locale.getDefault(), "Área: %.2f", area));
        perimeterText.setText(String.format(Locale.getDefault(), "Perímetro: %.2f", perimeter));
    }
}
