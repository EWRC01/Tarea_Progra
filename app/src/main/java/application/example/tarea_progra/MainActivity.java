package application.example.tarea_progra;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    DB db;
    public Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        obtenerDatos();
    }
    @Override
    public  void  onCreateContextMenu(ContextMenu menu, View v,
                                      ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflate= getMenuInflater();
        inflate.inflate(R.menu.mimenu, menu);
        AdapterView.AdapterContextMenuInfo info=
                (AdapterView.AdapterContextMenuInfo)menuInfo;
        c.moveToPosition((info.position));
        menu.setHeaderTitle(c.getString(1));
    }
    @Override
    public  boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.mnxModificar:
                try {
                    String user[]={
                            c.getString(1),
                            c.getString(2),
                            c.getString(3)
                    };
                    Bundle bundle= new Bundle();
                    bundle.putString("accion", "modificar");
                    bundle.putString("id", c.getString(0));
                    bundle.putStringArray("user", user);
                    Intent iusuario= new Intent(MainActivity.this,
                            agregar_amigos.Agregar_amigos.class);
                    iusuario.putExtras(bundle);
                    startActivity(iusuario);
                }catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
                    return true;
                    default:
                        return super.onContextItemSelected(item);
                }
        }
        public  AlertDialog eliminar(){
        AlertDialog.Builder confirmacion= new
                AlertDialog.Builder(MainActivity.this);
        confirmacion.setTitle(c.getString(1));
        confirmacion.setMessage("Esta seguro de eliminar este registro");
        confirmacion.setPositiveButton("Si", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.eliminarUsuario(c.getString(0));
                        dialog.cancel();
                        Toast.makeText(MainActivity.this, "El registro se elimino" +
                                "satisfactoriamente. ", Toast.LENGTH_LONG).show();
                    }


                });
        confirmacion.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(MainActivity.this, "Accion cancelada por el " +
                                "usuario.", Toast.LENGTH_LONG).show();

                    }
                });
        return confirmacion.create();
    }
    public  void  obtenerDatos(){
        db = new DB(MainActivity.this, "", null, 1);
        c=db.consultarUsuarios();
        if(c.moveToFirst()) {
            ListView ltsUser = (ListView) findViewById((R.id.ltsAmmigos));
            final ArrayList<String> alUSers = new ArrayList<String>();
            final ArrayAdapter<String> aaUsers = new
                    ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, alUSers);
            ltsUser.setAdapter(aaUsers);
            do {
                alUSers.add(c.getString(1));
            } while (c.moveToNext());
            aaUsers.notifyDataSetChanged();
            registerForContextMenu(ltsUser);
        }else   {
            Toast.makeText(MainActivity.this, "No hay registros que mostrar", Toast.LENGTH_LONG).show();
        }
    }
    public void registrar_amigos(View v){
        Intent iagregar= new Intent(MainActivity.this, agregar_amigos.Agregar_amigos.class);
        startActivity(iagregar);
    }
}