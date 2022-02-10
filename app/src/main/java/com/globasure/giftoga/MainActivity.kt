package com.globasure.giftoga

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.globasure.giftoga.constant.Constant
import com.globasure.giftoga.databinding.ActivityMainBinding
import com.globasure.giftoga.ui.base.BaseActivity
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.main_fragment.account.AccountFragment
import com.globasure.giftoga.ui.screen.main_fragment.buy_cards.BuyCardsFragment
import com.globasure.giftoga.ui.screen.main_fragment.home.HomeFragment
import com.globasure.giftoga.ui.screen.main_fragment.my_cards.MyCardsFragment
import com.globasure.giftoga.ui.screen.main_fragment.settings.SettingsFragment
import com.globasure.giftoga.utils.extension.gone
import com.globasure.giftoga.utils.extension.visible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavLogger
import com.ncapdevi.fragnav.FragNavSwitchController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>(), MainActivityView,
    BaseFragment.FragmentNavigation,
    FragNavController.TransactionListener, FragNavController.RootFragmentListener {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun getLayoutId() = R.layout.activity_main

    override fun getViewModel(): MainActivityViewModel = mainActivityViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getColorStatusBar(): Int = R.color.white

    val fragNavController: FragNavController =
        FragNavController(supportFragmentManager, R.id.container)


    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewModel().setView(this)
    }

    fun showBuyCardsScreen() {
        val options = FragNavTransactionOptions.newBuilder()
        options.customAnimations(
            R.anim.slide_in_from_bottom,
            R.anim.slide_out_to_top,
            R.anim.slide_in_from_top,
            R.anim.slide_out_to_bottom
        )
        fragNavController.pushFragment(BuyCardsFragment.newInstance(), options.build())
    }

    override fun initView() {
//        if (!RemoteConfigUtils.getVersion().contentEquals(BuildConfig.VERSION_NAME)){
//            onUpdateNeeded()
//        }

//        registerFCM()

        viewDataBinding.navigationView.setOnNavigationItemSelectedListener(
            onNavigationItemSelectedListener
        )
        viewDataBinding.navigationView.apply {
            buttonClickListener = {
                showBuyCardsScreen()
            }
        }

        fragNavController.apply {
            transactionListener = this@MainActivity
            rootFragmentListener = this@MainActivity
            createEager = true
            fragNavLogger = object : FragNavLogger {
                override fun error(message: String, throwable: Throwable) {
                    Timber.e(message)
                }
            }

            defaultTransactionOptions = FragNavTransactionOptions.newBuilder().transition(
                FragmentTransaction.TRANSIT_NONE
            ).build()
            fragmentHideStrategy = FragNavController.DETACH_ON_NAVIGATE_HIDE_ON_SWITCH

            navigationStrategy = UniqueTabHistoryStrategy(object : FragNavSwitchController {
                override fun switchTab(index: Int, transactionOptions: FragNavTransactionOptions?) {
                    when (index) {
                        Constant.INDEX_HOME -> pushFragment(HomeFragment.newInstance())
                        Constant.INDEX_ACCOUNT -> pushFragment(HomeFragment.newInstance())
                        Constant.INDEX_MY_CARDS -> pushFragment(MyCardsFragment.newInstance())
                        Constant.INDEX_SETTINGS -> pushFragment(SettingsFragment.newInstance())
                    }
                }
            })
        }

        fragNavController.initialize(Constant.INDEX_HOME, null)
        viewDataBinding.navigationView.setOnNavigationItemReselectedListener {
            fragNavController.clearStack()
        }
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    setStatusBarColor(R.color.app_back_ground_color)
                    fragNavController.switchTab(Constant.INDEX_HOME)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_account -> {
                    setStatusBarColor(R.color.white)
                    fragNavController.switchTab(Constant.INDEX_ACCOUNT)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_biller -> {
                    setStatusBarColor(R.color.white)
                    fragNavController.switchTab(Constant.INDEX_MY_CARDS)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_settings -> {
                    setStatusBarColor(R.color.white)
                    fragNavController.switchTab(Constant.INDEX_SETTINGS)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun pushFragment(fragment: Fragment, sharedElementList: List<Pair<View, String>>?) {
        val options = FragNavTransactionOptions.newBuilder()
        options.customAnimations(
            R.anim.slide_in_from_right,
            R.anim.slide_out_to_left,
            R.anim.slide_in_from_left,
            R.anim.slide_out_to_right
        )
        sharedElementList?.let {
            it.forEach { pair ->
                options.addSharedElement(pair)
            }
        }
        fragNavController.pushFragment(fragment, options.build())
    }

    override fun onBackPressed() {
        fragNavController.currentFrag?.let {
            val isRootFragment =
                (it is AccountFragment || it is MyCardsFragment || it is SettingsFragment)
            val isRootHomeFragment = it is HomeFragment
            val isBuyCardsFragment = it is BuyCardsFragment
            when {
                isRootHomeFragment -> finish()
                isRootFragment -> {
                    switchBackToHomePage()
                }
                else -> {
                    val options = FragNavTransactionOptions.newBuilder()
                    if (isBuyCardsFragment) {
                        options.customAnimations(
                            R.anim.hold,
                            R.anim.slide_out_to_top,
                            R.anim.hold,
                            R.anim.slide_out_to_bottom
                        )
                    } else {
                        options.customAnimations(
                            R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left,
                            R.anim.slide_in_from_left,
                            R.anim.slide_out_to_right
                        )
                    }
                    fragNavController.popFragment(options.build())
                }
            }
        }

        if (fragNavController.currentFrag == null) {
            super.onBackPressed()
        }
    }

    override val numberOfRootFragments: Int = 4


    override fun getRootFragment(index: Int): Fragment {
        when (index) {
            Constant.INDEX_HOME -> return HomeFragment.newInstance()
            Constant.INDEX_ACCOUNT -> return AccountFragment.newInstance()
            Constant.INDEX_MY_CARDS -> return MyCardsFragment.newInstance()
            Constant.INDEX_SETTINGS -> return SettingsFragment.newInstance()
        }
        throw IllegalStateException("Need to send an index that we know")

    }

    override fun onFragmentTransaction(
        fragment: Fragment?,
        transactionType: FragNavController.TransactionType
    ) {
        Timber.d("- onFragmentTransaction - ${fragment?.tag}")
    }

    override fun onTabTransaction(fragment: Fragment?, index: Int) {
        Timber.d("-onTabTransaction - ${fragment?.tag}")
    }

    fun switchBackToHomePage() {
        fragNavController.switchTab(Constant.INDEX_HOME)
        viewDataBinding.navigationView.selectedItemId = R.id.navigation_home
    }

    fun showHideNavigationBottom(isShow: Boolean) {
        if (isShow) {
            viewDataBinding.navigationView.visible()
            viewDataBinding.navigationView
                .animate()
                .translationY(0f)
                .setDuration(300)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        viewDataBinding.navigationView.visible()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                })
        } else {
            viewDataBinding.navigationView
                .animate()
                .translationY(viewDataBinding.navigationView.height.toFloat())
                .setDuration(300)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        viewDataBinding.navigationView.gone()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }
                })

        }
    }

    companion object {
        fun callingIntent(context: Context, withFlag: Boolean): Intent {
            val intent = Intent(context, MainActivity::class.java)
            if (withFlag) {
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            return intent
        }
    }
}