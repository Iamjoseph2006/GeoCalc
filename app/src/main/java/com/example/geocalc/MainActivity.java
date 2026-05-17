package com.example.geocalc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Actividad principal de GeoCalc.
 * Muestra cuatro pestañas con TabHost para calcular áreas y perímetros.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configurarPestanas();
        configurarCalculadoraCuadrado();
        configurarCalculadoraTriangulo();
        configurarCalculadoraCirculo();
        configurarCalculadoraRectangulo();
    }

    /**
     * Configura el TabHost con las cuatro figuras geométricas disponibles.
     */
    private void configurarPestanas() {
        TabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();

        agregarPestana(tabHost, "cuadrado", "Cuadrado", R.id.tab_square);
        agregarPestana(tabHost, "triangulo", "Triángulo", R.id.tab_triangle);
        agregarPestana(tabHost, "circulo", "Círculo", R.id.tab_circle);
        agregarPestana(tabHost, "rectangulo", "Rectángulo", R.id.tab_rectangle);
    }

    /**
     * Agrega una pestaña al TabHost con su etiqueta y contenido asociado.
     */
    private void agregarPestana(TabHost tabHost, String tag, String indicador, int contenidoId) {
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setIndicator(indicador);
        tabSpec.setContent(contenidoId);
        tabHost.addTab(tabSpec);
    }

    /**
     * Configura el cálculo del cuadrado: área = lado² y perímetro = 4 * lado.
     */
    private void configurarCalculadoraCuadrado() {
        EditText editLado = findViewById(R.id.edit_square_side);
        TextView textArea = findViewById(R.id.text_square_area);
        TextView textPerimetro = findViewById(R.id.text_square_perimeter);
        Button botonCalcular = findViewById(R.id.button_square_calculate);

        botonCalcular.setOnClickListener(v -> {
            Double lado = obtenerValorPositivo(editLado, "Ingrese un lado mayor que cero");
            if (lado == null) {
                return;
            }

            double area = lado * lado;
            double perimetro = 4 * lado;
            mostrarResultados(textArea, textPerimetro, area, perimetro);
        });
    }

    /**
     * Configura el cálculo del triángulo: área = (base * altura) / 2
     * y perímetro = base + lado2 + lado3.
     */
    private void configurarCalculadoraTriangulo() {
        EditText editBase = findViewById(R.id.edit_triangle_base);
        EditText editAltura = findViewById(R.id.edit_triangle_height);
        EditText editLadoDos = findViewById(R.id.edit_triangle_side_two);
        EditText editLadoTres = findViewById(R.id.edit_triangle_side_three);
        TextView textArea = findViewById(R.id.text_triangle_area);
        TextView textPerimetro = findViewById(R.id.text_triangle_perimeter);
        Button botonCalcular = findViewById(R.id.button_triangle_calculate);

        botonCalcular.setOnClickListener(v -> {
            Double base = obtenerValorPositivo(editBase, "Ingrese una base mayor que cero");
            Double altura = obtenerValorPositivo(editAltura, "Ingrese una altura mayor que cero");
            Double ladoDos = obtenerValorPositivo(editLadoDos, "Ingrese un lado 2 mayor que cero");
            Double ladoTres = obtenerValorPositivo(editLadoTres, "Ingrese un lado 3 mayor que cero");

            if (base == null || altura == null || ladoDos == null || ladoTres == null) {
                return;
            }

            double area = (base * altura) / 2;
            double perimetro = base + ladoDos + ladoTres;
            mostrarResultados(textArea, textPerimetro, area, perimetro);
        });
    }

    /**
     * Configura el cálculo del círculo: área = PI * radio² y perímetro = 2 * PI * radio.
     */
    private void configurarCalculadoraCirculo() {
        EditText editRadio = findViewById(R.id.edit_circle_radius);
        TextView textArea = findViewById(R.id.text_circle_area);
        TextView textPerimetro = findViewById(R.id.text_circle_perimeter);
        Button botonCalcular = findViewById(R.id.button_circle_calculate);

        botonCalcular.setOnClickListener(v -> {
            Double radio = obtenerValorPositivo(editRadio, "Ingrese un radio mayor que cero");
            if (radio == null) {
                return;
            }

            double area = Math.PI * Math.pow(radio, 2);
            double perimetro = 2 * Math.PI * radio;
            mostrarResultados(textArea, textPerimetro, area, perimetro);
        });
    }

    /**
     * Configura el cálculo del rectángulo: área = base * altura
     * y perímetro = 2 * (base + altura).
     */
    private void configurarCalculadoraRectangulo() {
        EditText editBase = findViewById(R.id.edit_rectangle_base);
        EditText editAltura = findViewById(R.id.edit_rectangle_height);
        TextView textArea = findViewById(R.id.text_rectangle_area);
        TextView textPerimetro = findViewById(R.id.text_rectangle_perimeter);
        Button botonCalcular = findViewById(R.id.button_rectangle_calculate);

        botonCalcular.setOnClickListener(v -> {
            Double base = obtenerValorPositivo(editBase, "Ingrese una base mayor que cero");
            Double altura = obtenerValorPositivo(editAltura, "Ingrese una altura mayor que cero");
            if (base == null || altura == null) {
                return;
            }

            double area = base * altura;
            double perimetro = 2 * (base + altura);
            mostrarResultados(textArea, textPerimetro, area, perimetro);
        });
    }

    /**
     * Valida y convierte el texto de un EditText a número positivo.
     * No acepta campos vacíos, valores no numéricos, cero ni negativos.
     */
    private Double obtenerValorPositivo(EditText editText, String mensajeError) {
        String texto = editText.getText().toString().trim();

        if (texto.isEmpty()) {
            editText.setError("Campo obligatorio");
            editText.requestFocus();
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return null;
        }

        try {
            double valor = Double.parseDouble(texto);
            if (valor <= 0) {
                editText.setError(mensajeError);
                editText.requestFocus();
                Toast.makeText(this, "No se aceptan valores negativos o cero", Toast.LENGTH_SHORT).show();
                return null;
            }

            editText.setError(null);
            return valor;
        } catch (NumberFormatException exception) {
            editText.setError("Ingrese un número válido");
            editText.requestFocus();
            Toast.makeText(this, "Ingrese solo números válidos", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * Muestra los resultados formateados con dos decimales en los TextViews.
     */
    private void mostrarResultados(TextView textArea, TextView textPerimetro, double area, double perimetro) {
        textArea.setText(String.format(Locale.getDefault(), "Área: %.2f", area));
        textPerimetro.setText(String.format(Locale.getDefault(), "Perímetro: %.2f", perimetro));
    }
}
