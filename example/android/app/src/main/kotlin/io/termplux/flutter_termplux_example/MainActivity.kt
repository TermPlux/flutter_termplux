package io.termplux.flutter_termplux_example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.app.FlutterApplication
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant
import io.termplux.flutter_termplux.FlutterTermPluxPlugin
import java.lang.ref.WeakReference

class MainActivity : FlutterTermPluxPlugin() {

    private var mainFragment: MainFragment? = null

    private lateinit var mFragmentManager: FragmentManager
    private lateinit var newMainFragment: MainFragment

    override fun initFlutterBoost(application: FlutterApplication) {
        super.initFlutterBoost(application)
        WeakReference(application).get()?.apply {
            FlutterBoost.instance().setup(
                this@apply,
                this@MainActivity,
                this@MainActivity
            )
        }
    }

    override fun initFlutterPlugin(engine: FlutterEngine?) {
        super.initFlutterPlugin(engine)
        engine?.let {
            GeneratedPluginRegistrant.registerWith(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        mFragmentManager = supportFragmentManager
        mainFragment = mFragmentManager.findFragmentByTag(
            tagFlutterBoostFragment
        ) as MainFragment?
        newMainFragment = FlutterBoostFragment.CachedEngineFragmentBuilder(
            MainFragment::class.java
        )
            .destroyEngineWithFragment(false)
            .renderMode(RenderMode.surface)
            .transparencyMode(TransparencyMode.opaque)
            .shouldAttachEngineToActivity(true)
            .build()
        if (mainFragment == null) {
            mainFragment = newMainFragment
            mFragmentManager
                .beginTransaction()
                .add(
                    R.id.fragmentContainerView,
                    newMainFragment,
                    tagFlutterBoostFragment
                )
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainFragment = null
    }

    internal class MainFragment : FlutterBoostFragment() {

        private val mTermPlux: FlutterTermPluxPlugin = FlutterTermPluxPlugin()

        override fun configureFlutterEngine(
            flutterEngine: FlutterEngine
        ) = super.configureFlutterEngine(
            flutterEngine
        ).also {
            mTermPlux.configure(
                flutterEngine = flutterEngine
            )
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = mTermPlux.attach(
            view = super.onCreateView(
                inflater,
                container,
                savedInstanceState
            ),
            flutterFragment = this@MainFragment
        )

        override fun cleanUpFlutterEngine(
            flutterEngine: FlutterEngine
        ) = super.cleanUpFlutterEngine(
            flutterEngine
        ).also {
            mTermPlux.clean(
                flutterEngine = flutterEngine
            )
        }
    }

    companion object {
        const val tagFlutterBoostFragment: String = "flutter_boost_fragment"
    }
}