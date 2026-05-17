package com.example.geocalc;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Actividad principal de GeoCalc.
 * Muestra cuatro pestañas con TabHost para calcular áreas y perímetros.
 */
public class MainActivity extends Activity {

    private static final int EXTRA_SAFE_TOP_DP = 18;
    private static final int EXTRA_SAFE_BOTTOM_DP = 16;

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configurarZonaSegura();
        configurarPestanas();
        configurarCalculadoraCuadrado();
        configurarCalculadoraTriangulo();
        configurarCalculadoraCirculo();
        configurarCalculadoraRectangulo();
    }

    /**
     * Respeta las barras del sistema y evita que el título quede debajo del notch o la cámara.
     */
    private void configurarZonaSegura() {
        View contenedorPrincipal = findViewById(R.id.main_container);
        int paddingHorizontal = dpToPx(16);
        int paddingTopBase = dpToPx(EXTRA_SAFE_TOP_DP);
        int paddingBottomBase = dpToPx(EXTRA_SAFE_BOTTOM_DP);

        contenedorPrincipal.setOnApplyWindowInsetsListener((view, insets) -> {
            view.setPadding(
                    paddingHorizontal,
                    paddingTopBase + insets.getSystemWindowInsetTop(),
                    paddingHorizontal,
                    paddingBottomBase + insets.getSystemWindowInsetBottom()
            );
            return insets;
        });
        contenedorPrincipal.requestApplyInsets();
    }

    /**
     * Configura el TabHost con las cuatro figuras geométricas disponibles.
     */
    private void configurarPestanas() {
        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();

        agregarPestana("cuadrado", "Cuadrado", R.id.tab_square);
        agregarPestana("triangulo", "Triángulo", R.id.tab_triangle);
        agregarPestana("circulo", "Círculo", R.id.tab_circle);
        agregarPestana("rectangulo", "Rectángulo", R.id.tab_rectangle);

        TabWidget tabWidget = tabHost.getTabWidget();
        tabWidget.setStripEnabled(false);
        ajustarSeparacionPestanas(tabWidget);
        actualizarEstiloPestanas(tabHost.getCurrentTab());

        tabHost.setOnTabChangedListener(tabId -> actualizarEstiloPestanas(tabHost.getCurrentTab()));
    }

    /**
     * Agrega una pestaña al TabHost con su etiqueta y contenido asociado.
     */
    private void agregarPestana(String tag, String indicador, int contenidoId) {
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setIndicator(crearIndicadorPestana(indicador));
        tabSpec.setContent(contenidoId);
        tabHost.addTab(tabSpec);
    }

    /**
     * Crea una etiqueta compacta y legible para cada pestaña.
     */
    private TextView crearIndicadorPestana(String texto) {
        TextView indicador = new TextView(this);
        indicador.setGravity(Gravity.CENTER);
        indicador.setSingleLine(true);
        indicador.setIncludeFontPadding(false);
        indicador.setMinHeight(dpToPx(42));
        indicador.setPadding(dpToPx(6), dpToPx(10), dpToPx(6), dpToPx(10));
        indicador.setText(texto);
        indicador.setTextSize(12);
        indicador.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return indicador;
    }

    /**
     * Da el mismo ancho a cada pestaña y agrega separación visual entre ellas.
     */
    private void ajustarSeparacionPestanas(TabWidget tabWidget) {
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            View tab = tabWidget.getChildAt(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dpToPx(42), 1f);
            params.setMargins(dpToPx(3), 0, dpToPx(3), 0);
            tab.setLayoutParams(params);
        }
    }

    /**
     * Resalta la pestaña activa con el color principal de la aplicación.
     */
    private void actualizarEstiloPestanas(int posicionActiva) {
        TabWidget tabWidget = tabHost.getTabWidget();
        int colorActivo = getColor(R.color.white);
        int colorInactivo = getColor(R.color.geocalc_primary_dark);

        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            View tab = tabWidget.getChildAt(i);
            boolean activa = i == posicionActiva;
            tab.setBackgroundResource(activa ? R.drawable.bg_tab_active : R.drawable.bg_tab_inactive);

            if (tab instanceof TextView) {
                TextView textoTab = (TextView) tab;
                textoTab.setTextColor(activa ? colorActivo : colorInactivo);
            }
        }
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
     * Muestra los resultados formateados con dos decimales y unidades.
     */
    private void mostrarResultados(TextView textArea, TextView textPerimetro, double area, double perimetro) {
        textArea.setText(String.format(Locale.getDefault(), "Área: %.2f u²", area));
        textPerimetro.setText(String.format(Locale.getDefault(), "Perímetro: %.2f u", perimetro));
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
