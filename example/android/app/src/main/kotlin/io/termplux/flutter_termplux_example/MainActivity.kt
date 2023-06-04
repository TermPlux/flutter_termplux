package io.termplux.flutter_termplux_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode

class MainActivity : AppCompatActivity() {

    private var mainFragment: MainFragment? = null

    private lateinit var mFragmentManager: FragmentManager
    private lateinit var newMainFragment: MainFragment

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

    override fun onDestroy() {
        super.onDestroy()
        mainFragment = null
    }

    companion object {
        const val tagFlutterBoostFragment: String = "flutter_boost_fragment"
    }
}