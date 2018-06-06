package com.example.sopadeletras;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Sopa extends AppCompatActivity {
    private final int ids[][] = {
            {R.id.boton00, R.id.boton01,R.id.boton02,R.id.boton03,R.id.boton04,R.id.boton05, R.id.boton06, R.id.boton07,R.id.boton08,R.id.boton09},
            {R.id.boton10, R.id.boton11,R.id.boton12,R.id.boton13,R.id.boton14,R.id.boton15, R.id.boton16, R.id.boton17,R.id.boton18,R.id.boton19},
            {R.id.boton20, R.id.boton21,R.id.boton22,R.id.boton23,R.id.boton24,R.id.boton25, R.id.boton26, R.id.boton27,R.id.boton28,R.id.boton29},
            {R.id.boton30, R.id.boton31,R.id.boton32,R.id.boton33,R.id.boton34,R.id.boton35, R.id.boton36, R.id.boton37,R.id.boton38,R.id.boton39},
            {R.id.boton40, R.id.boton41,R.id.boton42,R.id.boton43,R.id.boton44,R.id.boton45, R.id.boton46, R.id.boton47,R.id.boton48,R.id.boton49},
            {R.id.boton50, R.id.boton51,R.id.boton52,R.id.boton53,R.id.boton54,R.id.boton55, R.id.boton56, R.id.boton57,R.id.boton58,R.id.boton59},
            {R.id.boton60, R.id.boton61,R.id.boton62,R.id.boton63,R.id.boton64,R.id.boton65, R.id.boton66, R.id.boton67,R.id.boton68,R.id.boton69},
            {R.id.boton70, R.id.boton71,R.id.boton72,R.id.boton73,R.id.boton74,R.id.boton75, R.id.boton76, R.id.boton77,R.id.boton78,R.id.boton79},
            {R.id.boton80, R.id.boton81,R.id.boton82,R.id.boton83,R.id.boton84,R.id.boton85, R.id.boton86, R.id.boton87,R.id.boton88,R.id.boton89},
            {R.id.boton90, R.id.boton91,R.id.boton92,R.id.boton93,R.id.boton94,R.id.boton95, R.id.boton96, R.id.boton97,R.id.boton98,R.id.boton99}
    };
    private Game juego;
    private int filaInicial;
    private int columnaInicial;
    private int filaFinal;
    private int columnaFinal;
    private boolean primero = true;
    String  nPalabras, json_url;
    private Palabras[] palabras;
    TextView contador,nCat;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ListaItem> myDataset = new ArrayList<ListaItem>();
    Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sopa);
        contexto = this;
        contador = findViewById(R.id.textNumeroPalabras);
        nCat = findViewById(R.id.ncat);
        Bundle extras = getIntent().getExtras();
        nPalabras=extras.getString("nPalabras");
        palabras = new Palabras[parseInt(nPalabras)];
        json_url= "http://cpd.iesgrancapitan.org:9101/~feosda/sopaletras/sopadeletras.php?categoria="+extras.getString("Categoria")+"&palabra="+nPalabras;
        nCat.setText(extras.getString("Nombre"));
        contador.setText(nPalabras);
        mRecyclerView = findViewById(R.id.palabras);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        PedirPalabras();
    }

    public void inicio(){
        juego = new Game(palabras, this);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                ImageView img = findViewById( ids[i][j] );
                String imageName = String.valueOf(juego.getLetra(i,j));
                int resID = getResources().getIdentifier(imageName, "drawable",  getPackageName());
                img.setImageResource(resID);
            }
        }
    }

    public void PedirPalabras() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(json_url,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0 ; i < response.length(); i++) {
                    JSONObject obj;
                    try {
                        obj = response.getJSONObject(i);
                        palabras[i] = new Palabras(obj.getString("palabra"));
                        String palabra = obj.getString("palabra");
                        String id = obj.getString("id");
                        myDataset.add(new ListaItem(palabra, id));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adaptador();
                inicio();
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
        MySingleton.getInstance(Sopa.this).addToRequestQueue(jsonArrayRequest);
    }

    public void onClick(View v){
        int id = v.getId();
        int[] selected = coorJuego(id);
        findViewById(id).setAlpha(Float.parseFloat("0.5"));
        if(primero){
            filaInicial = selected[0];
            columnaInicial = selected[1];
            primero = false;
        }else{
            filaFinal = selected[0];
            columnaFinal = selected[1];
            String solucion = juego.compruebaAcierto(filaInicial,columnaInicial,filaFinal,columnaFinal);
            correcta(solucion);

            findViewById(ids[filaInicial][columnaInicial]).setAlpha(1);
            findViewById(ids[filaFinal][columnaFinal]).setAlpha(1);
            filaInicial = -1;
            columnaInicial = -1;
            filaFinal = -1;
            columnaFinal = -1;
            primero = true;
            int i=0;
            int position=0;
            for (ListaItem palabra: myDataset) {
                System.out.println(i);
                if (palabra.getNombre()==solucion){
                    position=i;
                }
                i++;
            }
            if (solucion.equals("no")){
                System.out.println("no");
            }else {
                System.out.println(position+" "+solucion);
                myDataset.remove(position);
                removeElement(palabras, position);
                adaptador();
            }
        }
    }
    public void correcta(String correcto){
        if (correcto.equals("no")){
            Toast.makeText(this,"Te has equivocado",Toast.LENGTH_LONG).show();
        }else{
            Integer a = parseInt(contador.getText().toString())-1;
            contador.setText(Integer.toString(a));
            tacharLetra();
            if (contador.getText().toString().equals("0")){
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
                builder.setMessage("¡¡Has encontrado todas las palabras!!");
                builder.setTitle("Partida finalizada");
                builder.setPositiveButton("Nueva partida", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Categorias", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        onBackPressed();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
    public void tacharLetra(){
        if (columnaInicial==columnaFinal){
            //palabra vertical
            if (filaInicial<filaFinal) {
                palabraV(filaInicial,filaFinal);
            }else{
                palabraV(filaFinal,filaInicial);
            }
        } else if (filaInicial == filaFinal){
            //palabra horizontal
            if (columnaInicial<columnaFinal) {
                palabraH(columnaInicial,columnaFinal);
            }else{
                palabraH(columnaFinal,columnaInicial);
            }
        }else{
            //palabra diagonal
            if(filaInicial<filaFinal) {
                palabraD(columnaInicial,columnaFinal,filaInicial);
            }else{
                palabraD(columnaFinal,columnaInicial,filaFinal);
            }
        }
    }
    private void palabraV(int pinicio,int pfinal) {
        for (int i = pinicio; i <= pfinal; i++) {
            findViewById(ids[i][columnaInicial]).setBackground(getDrawable(R.drawable.lineavertical));
        }
    };
    private void palabraH(int finicio,int ffinal) {
        for (int i = finicio; i <= ffinal; i++) {
            findViewById(ids[filaInicial][i]).setBackground(getDrawable(R.drawable.lineahorizontal));
        }
    };
    private void palabraD(int cinicio,int cfinal,int ffinal) {
        int j = ffinal;
        for (int i = cinicio; i <= cfinal; i++) {
            findViewById(ids[j][i]).setBackground(getDrawable(R.drawable.lineadiagonal));
            j++;
        }
    };

    public int[] coorJuego(int id){
        int[] coord = new int[2];
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                if(ids[i][j] == id){
                    coord[0] = i;
                    coord[1] = j;
                }
            }
        }
        return coord;
    }

    public void adaptador(){
        mAdapter = new Adapter(myDataset);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(contexto, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        filaInicial=palabras[position].getFilaInicial();
                        columnaInicial=palabras[position].getColumnaInicial();
                        filaFinal=palabras[position].getFilaFinal();
                        columnaFinal=palabras[position].getColumnaFinal();
                        String pulsaratachar = juego.compruebaAcierto(filaInicial,columnaInicial,filaFinal,columnaFinal);
                        correcta(pulsaratachar);
                        myDataset.remove(position);
                        removeElement(palabras, position);
                        adaptador();
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
        mRecyclerView.setAdapter(mAdapter);
    }
    public void removeElement(Object[] arr, int removedIdx) {
        System.arraycopy(arr, removedIdx + 1, arr, removedIdx, arr.length - 1 - removedIdx);
    }
}