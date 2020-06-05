package cn.cslg.mooddiary.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import cn.cslg.mooddiary.R
import cn.cslg.mooddiary.utils.LogUtil
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private lateinit var fragView: View
    private lateinit var avatar: CircleImageView
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var cityInput: EditText
    private lateinit var saveBtn: Button


    private val FROM_ALBUM = 1
    private var uri: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragView = inflater.inflate(R.layout.profile_frag, container, false)
        initViews()
        return fragView
    }

    private fun initViews() {
        avatar = fragView.findViewById(R.id.avatar)
        avatar.isClickable = true
        nameInput = fragView.findViewById(R.id.nameInput)
        emailInput = fragView.findViewById(R.id.emailInput)
        cityInput = fragView.findViewById(R.id.cityInput)
        saveBtn = fragView.findViewById(R.id.saveBtn)
        uri = "default"
        restoreInfo()
        saveInfo()
        getAvatarFromAlbum()
    }

    private fun restoreInfo() {
        val prefs = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        if (prefs != null) {
            val savedUri = prefs.getString("avatar", "default")
            val name = prefs.getString("name", "未定义用户名")
            val email = prefs.getString("email", "未定义邮箱")
            val city = prefs.getString("city", "未定义城市名")
            if (savedUri != "default") {
                Glide.with(activity!!).load(Uri.parse(savedUri)).into(avatar)
            } else {
                Glide.with(activity!!).load(R.drawable.nav_icon).into(avatar)
            }
            nameInput.setText(name)
            emailInput.setText(email)
            cityInput.setText(city)
        }
    }


    private fun getAvatarFromAlbum() {
        avatar.setOnClickListener {
            getPermission()
        }
    }

    private fun getPermission() {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            openAlbum()
        }
    }

    private fun openAlbum() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, FROM_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FROM_ALBUM -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let {
                        uri = it.toString()
                        LogUtil.d("Profile-Get", uri)
                        Glide.with(activity!!).load(it).into(avatar)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum()
                } else {
                    Toast.makeText(activity, "拒绝读取SD卡权限将无法自定义头像", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveInfo() {
        saveBtn.setOnClickListener {
            activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)?.edit {
                LogUtil.d("Profile-Save", uri)
                if (uri != "default") {
                    putString("avatar", uri)
                }
                putString("name", nameInput.text.toString())
                putString("email", emailInput.text.toString())
                putString("city", cityInput.text.toString())
                Toast.makeText(activity, "修 改 完 成!", Toast.LENGTH_SHORT).show()
                val intent = Intent("cn.cslg.mooddiary.REFRESH")
                activity?.sendBroadcast(intent)
            }
        }
    }
}