package com.app.gmv3.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.gmv3.R;
import com.app.gmv3.adapters.AdapterPerfilLotes;
import com.app.gmv3.models.Facturas_mora;
import com.app.gmv3.models.Moras;
import com.app.gmv3.utilities.MyDividerItemDecoration;
import com.app.gmv3.utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.gmv3.utilities.Constant.GET_PROFIL_USER;
import static com.app.gmv3.utilities.Constant.PUSH_PIN;


public class ActivityPerfilCliente extends AppCompatActivity{
    TextView txt_perfil_name_cliente,txt_perfil_disponible,txt_perfil_saldo,txt_perfil_limite;
    TextView txt_perfil_noVencido,txt_perfil_d30,txt_perfil_d60,txt_perfil_d90,txt_perfil_d120,txt_perfil_m120,
             txt_perfil_d45,txt_perfil_d150,txt_perfil_m150;
    TextView txt_tele,txt_condicion_pago;
    String code_cliente;

    public static ArrayList<String> factura_id = new ArrayList<String>();
    public static ArrayList<String> factura_date = new ArrayList<String>();
    public static ArrayList<String> factura_cant = new ArrayList<String>();
    public static ArrayList<String> factura_monto = new ArrayList<String>();
    View lyt_empty_history;
    RecyclerView recyclerView;
    AdapterPerfilLotes recyclerAdaptePerfilFactura;
    List<Facturas_mora> arrayItemLotes;
    CircleImageView ImgVerication;
    String strVerificado,strPin;

    private Menu menu_pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lyt_empty_history = findViewById(R.id.lyt_empty_result);

        txt_perfil_name_cliente = findViewById(R.id.id_perfil_name_cliente);
        txt_perfil_disponible   = findViewById(R.id.id_perfil_disponible);
        txt_perfil_saldo        = findViewById(R.id.id_perfil_saldo);
        txt_perfil_limite       = findViewById(R.id.id_perfil_limite);

        txt_tele                = findViewById(R.id.id_tele);
        txt_condicion_pago      = findViewById(R.id.id_condicion_pago);

        txt_perfil_noVencido    = findViewById(R.id.id_perfil_noVencido);
        txt_perfil_d30          = findViewById(R.id.id_perfil_d30);
        txt_perfil_d45          = findViewById(R.id.id_perfil_d45);
        txt_perfil_d60          = findViewById(R.id.id_perfil_d60);
        txt_perfil_d90          = findViewById(R.id.id_perfil_d90);
        txt_perfil_d120         = findViewById(R.id.id_perfil_d120);
        txt_perfil_d150         = findViewById(R.id.id_perfil_d150);
        txt_perfil_m150         =findViewById(R.id.id_perfil_m150);
        //txt_perfil_m120         = findViewById(R.id.id_perfil_m120);

        ImgVerication           = findViewById(R.id.id_btn_verificacion);

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL, 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerAdaptePerfilFactura = new AdapterPerfilLotes(this, arrayItemLotes);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.grey_5));
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),  new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ActivityPerfilCliente.this, ActivityViewFactura.class);
                        intent.putExtra("factura_id",factura_id.get(position));
                        intent.putExtra("factura_date",factura_date.get(position));
                        startActivity(intent);
                    }
                }, 400);
            }
        }));


        Utils.setSystemBarLight(this);




        Intent intent = getIntent();


        strVerificado = intent.getStringExtra("Verificado");
        strPin = intent.getStringExtra("pin");

        code_cliente = intent.getStringExtra("Client_Code");
        txt_perfil_name_cliente.setText(intent.getStringExtra("CLient_name"));
        txt_tele.setText(intent.getStringExtra("Telefono"));
        txt_condicion_pago.setText(intent.getStringExtra("Condicion_pago"));
        txt_perfil_disponible.setText(("C$ ").concat(intent.getStringExtra("Disponible")));
        txt_perfil_saldo.setText(("C$ ").concat(intent.getStringExtra("Saldo")));
        txt_perfil_limite.setText(("C$ ").concat(intent.getStringExtra("Limite")));

        ImgVerication.setImageDrawable(getApplicationContext().getResources().getDrawable(((strVerificado.contains("S;")) ? R.drawable.verificado :R.drawable.noverificado)));

        ImgVerication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityPerfilCliente.this, ActivityVerificacion.class);
                intent.putExtra("Codi_cliente",code_cliente);
                startActivity(intent);
            }
        });

        findViewById(R.id.id_history_last_3m).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityPerfilCliente.this, ActivityUltimos3m.class);

                intent.putExtra("factura_id",code_cliente);
                intent.putExtra("Name_cliente",txt_perfil_name_cliente.getText());

                startActivity(intent);
            }
        });
        findViewById(R.id.id_history_nc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityPerfilCliente.this, ActivityNotaCredito.class);

                intent.putExtra("factura_id",code_cliente);
                intent.putExtra("Name_cliente",txt_perfil_name_cliente.getText());

                startActivity(intent);
            }
        });

        findViewById(R.id.id_search_lotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityPerfilCliente.this, ActivitySearchLotes.class);

                intent.putExtra("factura_id",code_cliente);
                startActivity(intent);
            }
        });
        fetchData();

    }
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final ClickListener clickListener) {

            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener {
        public void onClick(View view, int position);
    }
    public void clearData() {
        factura_id.clear();
        factura_date.clear();
        factura_cant.clear();
        factura_monto.clear();
    }

    private void fetchData() {
        JsonArrayRequest request = new JsonArrayRequest(GET_PROFIL_USER + code_cliente, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), R.string.failed_fetch_data, Toast.LENGTH_LONG).show();
                    return;
                }

                List<Moras> items = new Gson().fromJson(response.toString(), new TypeToken<List<Moras>>() {
                }.getType());

                if (items.size() > 0) {
                    lyt_empty_history.setVisibility(View.GONE);
                    txt_perfil_noVencido.setText(("C$ ").concat(items.get(0).getNoVencidos()));
                    txt_perfil_d30.setText(("C$ ").concat(items.get(0).getDias30()));
                    txt_perfil_d45.setText(("C$ ").concat(items.get(0).getDias45()));
                    txt_perfil_d60.setText(("C$ ").concat(items.get(0).getDias60()));
                    txt_perfil_d90.setText(("C$ ").concat(items.get(0).getDias90()));
                    txt_perfil_d120.setText(("C$ ").concat(items.get(0).getDias120()));
                    txt_perfil_d150.setText(("C$").concat(items.get(0).getDias150()));
                    txt_perfil_m150.setText(("C$").concat(items.get(0).getMas150()));
                    //txt_perfil_m120.setText(("C$ ").concat(items.get(0).getMas120()));


                    clearData();
                    List<String> data = Arrays.asList(items.get(0).getFACT_PEND().split(","));

                    for (int i = 0; i < data.size(); i++) {

                        List<String> row = Arrays.asList(data.get(i).split(":"));
                        factura_id.add(row.get(0));
                        factura_cant.add(row.get(1));
                        factura_date.add(row.get(2));
                        factura_monto.add(row.get(3));

                    }
                    recyclerView.setAdapter(recyclerAdaptePerfilFactura);
                } else {
                    lyt_empty_history.setVisibility(View.VISIBLE);
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e("INFO", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }
    private void PushPin() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PUSH_PIN, new Response.Listener<String>() {
            @Override
            public void onResponse(final String ServerResponse) {
                if (ServerResponse == null) {
                    Toast.makeText(getApplicationContext(), R.string.failed_fetch_data, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ActivityPerfilCliente.this, ServerResponse, Toast.LENGTH_SHORT).show();

                    if (ServerResponse.equals("Fijado")){
                        ((menu_pin.findItem(R.id.item_pin))).setIcon(R.drawable.ic_unpin);
                    }else{
                        ((menu_pin.findItem(R.id.item_pin))).setIcon(R.drawable.ic_pin);
                    }
                }
            }
            },
            new Response.ErrorListener() {
                @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
            }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cliente", code_cliente);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.item_pin:
                PushPin();
                break;
            case R.id.item_location:
                List<String> row = Arrays.asList(strVerificado.split(";"));
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", Double.parseDouble(row.get(1)),Double.parseDouble(row.get(2)));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {



        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_perfil_cliente, menu);
        MenuItem Item_rptVisita = menu.findItem(R.id.item_rpt_visita);
        MenuItem Item_location = menu.findItem(R.id.item_location);

        this.menu_pin = menu;

        if (strPin.equals("S")){
            ((menu.findItem(R.id.item_pin))).setIcon(R.drawable.ic_unpin);
        }else{
            ((menu.findItem(R.id.item_pin))).setIcon(R.drawable.ic_pin);
        }


        // show the button when some condition is true
        if (strVerificado.contains("S;")) {
           // Item_rptVisita.setVisible(true);
            Item_location.setVisible(true);
        }else{
            //Item_rptVisita.setVisible(false);
            Item_location.setVisible(false);

        }
        return true;
    }
}

