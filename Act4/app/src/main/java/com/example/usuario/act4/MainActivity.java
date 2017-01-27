package com.example.usuario.act4;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private TextView tvTitulo;
    private TextView tvNom;
    private TextView tvEdad;
    private TextView tvCiclo;
    private TextView tvCurso;
    private TextView tvOtro;
    private TextView tvResultado;

    private EditText edNom;
    private EditText edEdad;
    private EditText edCiclo;
    private EditText edCurso;
    private EditText edOtro;

    private Button btnInsertar;
    private Button btnSuprBD;
    private Button btnEliminar;
    private Button btnConsultar;
    private Button btnInterfaz;

    private SQLiteDatabase db;
    private String elec;
    private String con;
    private String con2;

   private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edNom = (EditText) findViewById(R.id.edNom);
        edEdad = (EditText) findViewById(R.id.edEdad);
        edCiclo = (EditText) findViewById(R.id.edCiclo);
        edCurso = (EditText) findViewById(R.id.edCurso);
        edOtro = (EditText) findViewById(R.id.edOtro);

        tvTitulo = (TextView) findViewById(R.id.tvTitulo);
        tvNom = (TextView) findViewById(R.id.tvNom);
        tvEdad = (TextView) findViewById(R.id.tvEdad);
        tvCiclo = (TextView) findViewById(R.id.tvCiclo);
        tvCurso = (TextView) findViewById(R.id.tvCurso);
        tvOtro = (TextView) findViewById(R.id.tvOtro);
        tvResultado = (TextView) findViewById(R.id.tvResultado);

        btnInsertar = (Button)findViewById(R.id.btnInsertar);
        btnSuprBD = (Button)findViewById(R.id.btnSuprBD);
        btnEliminar = (Button)findViewById(R.id.btnEliminar);
        btnConsultar = (Button)findViewById(R.id.btnConsultar);
        btnInterfaz = (Button)findViewById(R.id.btnInterfaz);

        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        UsuariosSQLiteHelper usdbh =
                new UsuariosSQLiteHelper(this, "DBUsuarios", null, 1);

        db = usdbh.getWritableDatabase();
        desactivarBotones();

        btnInterfaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInterfaz();
            }
        });

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = edNom.getText().toString();
                String ed = edEdad.getText().toString();
                String ci = edCiclo.getText().toString();
                String cu = edCurso.getText().toString();
                String otr = edOtro.getText().toString();

                if(elec.equals("Estudiantes")) {
                    ContentValues nuevoRegistro = new ContentValues();
                    nuevoRegistro.put("nombre", nom);
                    nuevoRegistro.put("edad", ed);
                    nuevoRegistro.put("ciclo", ci);
                    nuevoRegistro.put("curso", cu);
                    nuevoRegistro.put("nota", otr);
                    db.insert("Estudiantes", null, nuevoRegistro);
                }

                if (elec.equals("Profesores")){
                    ContentValues nuevoRegistro = new ContentValues();
                    nuevoRegistro.put("nombre", nom);
                    nuevoRegistro.put("edad", ed);
                    nuevoRegistro.put("ciclo", ci);
                    nuevoRegistro.put("curso", cu);
                    nuevoRegistro.put("despacho", otr);
                    db.insert("Profesores", null, nuevoRegistro);
                }
                Toast toast =
                        Toast.makeText(getApplicationContext(),
                                "Usuario "+nom+" introducido correctamente", Toast.LENGTH_SHORT);

                toast.show();
                desactivarBotones();
                enBlanco();
            }
        });

        btnSuprBD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //db.execSQL("DROP DATABASE DBUsuarios");
                context.deleteDatabase("DBUsuarios");

                Toast toast =
                        Toast.makeText(getApplicationContext(),
                                "La base de datos a sido eliminada correactamente", Toast.LENGTH_SHORT);

                toast.show();

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Recuperamos los valores de los campos de texto
                String nom = edNom.getText().toString();
                String sql = "DELETE FROM Estudiantes WHERE nombre = '" + nom + "'" ;
                db.execSQL(sql);

                //db.delete("Estudiantes", "nom='" + nom + "'",null);

                Toast toast =
                        Toast.makeText(getApplicationContext(),
                                "Usuario "+nom+" eliminado correctamente", Toast.LENGTH_SHORT);

                toast.show();
                desactivarBotones();
                activarElements();
            }
        });

        /*String[] columnasBD = new String[] {"_id", "nom"};
        cursor = new MatrixCursor(columnasBD);*/

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String cicl = edCiclo.getText().toString();
                String curs = edCurso.getText().toString();

                Cursor c = null;
                if (con.equals("Estudiantes")){
                    c = db.rawQuery("SELECT id_Est, nombre FROM Estudiantes", null);
                }
                if (con.equals("Profesores")){
                    c = db.rawQuery("SELECT id_Prof, nombre FROM Profesores", null);
                }
                if (con.equals("Todos")){
                    c = db.rawQuery("SELECT id_Est, nombre FROM Estudiantes UNION SELECT id_Prof, nombre FROM Profesores",null);
                }
                if (con.equals("Ciclo")){

                    if(con2.equals("Estudiantes")) {
                        c = db.rawQuery("SELECT id_Est, nombre FROM Estudiantes WHERE ciclo LIKE '" + cicl + "'", null);
                    }else{
                        c = db.rawQuery("SELECT id_Prof, nombre FROM Profesores WHERE ciclo LIKE '" + cicl +"'", null);
                    }
                }
                if (con.equals("Curso")){

                    if(con2.equals("Estudiantes")) {
                        c = db.rawQuery("SELECT id_Est, nombre FROM Estudiantes WHERE curso LIKE '" + curs + "'", null);
                    }else{
                        c = db.rawQuery("SELECT id_Prof, nombre FROM Profesores WHERE curso LIKE '" + curs +"'", null);
                    }
                }


                //Recorremos los resultados para mostrarlos en pantalla
                tvResultado.setText("");
                if (c != null && c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        String id = c.getString(0);
                        String nom = c.getString(1);

                        //cursor.addRow(new Object[] {id,nom});
                        tvResultado.append(" " + id + " - " + nom + "\n");
                    } while(c.moveToNext());
                }

                desactivarBotones();
                activarElements();
            }
        });

       /* String[] desdeEstasColumnas = {"_id", "nom"};
        int[] aEstasViews = {R.id.tvId, R.id.tvNom};

        ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.recyclerview_usuario, cursor, desdeEstasColumnas, aEstasViews, 0);
        ListView listado = getListView();
        listado.setAdapter(adapter);*/
    }

    //Metodos con los diferentes dialogos ----------------------------------------------------------

    public void secDialog(){
        final CharSequence[] items = {"Estudiantes", "Profesores"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Que desea insertar?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(items[item].equals("Estudiantes")){
                    elec="Estudiantes";
                    textoEst();
                }
                if(items[item].equals("Profesores")){
                    elec="Profesores";
                    textoProf();
                }
                activarElements();
                activarBotones();
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void dialogInterfaz(){

        final CharSequence[] items = {"Insertar", "Eliminar", "Consultar","SuprBD"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Que desea hacer?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(items[item].equals("Insertar")){
                    secDialog();
                }
                if(items[item].equals("Eliminar")){
                    activarBotones();
                    desactivarElements();
                }
                if(items[item].equals("Consultar")){
                    dialogConsultas();
                    activarBotones();
                    desactivarElements();
                }
                if(items[item].equals("SuprBD")){
                    activarBotones();
                    desactivarElements();
                }
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void dialogConsultas(){

        final CharSequence[] items = {"Estudiantes", "Profesores", "Todos", "Ciclo", "Curso"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Que desea consultar?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(items[item].equals("Estudiantes")){
                    con="Estudiantes";
                    desactivarTodo();
                }
                if(items[item].equals("Profesores")){
                    con="Profesores";
                    desactivarTodo();
                }
                if(items[item].equals("Todos")){
                    con="Todos";
                    desactivarTodo();
                }
                if(items[item].equals("Ciclo")){
                    con="Ciclo";
                    menosCiclo();
                    dialogCon2();
                }
                if(items[item].equals("Curso")){
                    con="Curso";
                    menosCurso();
                    dialogCon2();
                }
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void dialogCon2(){
        final CharSequence[] items = {"Estudiantes", "Profesores"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sobre que deseea consultar el Curso/Ciclo?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(items[item].equals("Estudiantes")){
                    con2="Estudiantes";
                }
                if(items[item].equals("Profesores")){
                    con2="Profesores";
                }
                activarBotones();
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Metodos para activar, desactivar y modificar elementos del Activity --------------------------

    public void textoProf(){
        tvTitulo.setText("Profesores:");
        tvOtro.setText("Despacho:");
    }

    public void textoEst(){
        tvTitulo.setText("Estudiantes:");
        tvOtro.setText("Nota Media:");
    }

    public void desactivarElements() {
        edEdad.setEnabled(false);
        edCiclo.setEnabled(false);
        edCurso.setEnabled(false);
        edOtro.setEnabled(false);
    }

    public void desactivarTodo() {
        edNom.setEnabled(false);
        edEdad.setEnabled(false);
        edCiclo.setEnabled(false);
        edCurso.setEnabled(false);
        edOtro.setEnabled(false);
    }

    public void activarElements() {
        edNom.setEnabled(true);
        edEdad.setEnabled(true);
        edCiclo.setEnabled(true);
        edCurso.setEnabled(true);
        edOtro.setEnabled(true);
    }

    public void desactivarBotones(){
        btnInsertar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnConsultar.setEnabled(false);
        btnSuprBD.setEnabled(false);
    }

    public void activarBotones(){
        btnInsertar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnConsultar.setEnabled(true);
        btnSuprBD.setEnabled(true);
    }

    public void enBlanco() {
        edNom.setText("");
        edEdad.setText("");
        edCiclo.setText("");
        edCurso.setText("");
        edOtro.setText("");
    }

    public void menosCiclo() {
        edNom.setEnabled(false);
        edEdad.setEnabled(false);
        edCurso.setEnabled(false);
        edCiclo.setEnabled(true);
        edOtro.setEnabled(false);
    }

    public void menosCurso() {
        edNom.setEnabled(false);
        edEdad.setEnabled(false);
        edCurso.setEnabled(true);
        edCiclo.setEnabled(false);
        edOtro.setEnabled(false);
    }
}
