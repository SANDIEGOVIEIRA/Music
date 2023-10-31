package br.com.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrimeiroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrimeiroFragment extends Fragment {
    private WebView webView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PrimeiroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrimeiroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrimeiroFragment newInstance(String param1, String param2) {
        PrimeiroFragment fragment = new PrimeiroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {View rootView = inflater.inflate(R.layout.fragment_primeiro, container, false);

        // Inicialize a WebView
        webView = rootView.findViewById(R.id.webView);

        // Habilita JavaScript no WebView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        // Habilita a mistura de conteúdo de outras origens (incluindo arquivos CSS)
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // Configura a WebView para permitir o uso de protocolos não seguros (http) junto com protocolos seguros (https)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        // Define um WebViewClient para abrir links internamente no WebView
        webView.setWebViewClient(new WebViewClient());

        // Carrega o arquivo HTML a partir da pasta de recursos
        webView.loadUrl("file:///android_asset/TEST/index.html");

        return rootView;
    }
}
