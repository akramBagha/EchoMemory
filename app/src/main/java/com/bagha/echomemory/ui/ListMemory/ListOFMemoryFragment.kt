package com.bagha.echomemory.ui.ListMemory

import android.content.Intent
import android.os.Bundle
import android.os.DeadObjectException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bagha.echomemory.Adapter.Adapter_MemoryTitleList
import com.bagha.echomemory.B_Function.B_CheckPermission
import com.bagha.echomemory.B_Function.B_SelectImage
import com.bagha.echomemory.B_Function.B_SelectImage.Companion.mCurrentPhotoPath
import com.bagha.echomemory.B_Function.GifImageCall
import com.bagha.echomemory.B_Function.GlobalFunction
import com.bagha.echomemory.DB_Room.Model_Table.MemoInfo_Table
import com.bagha.echomemory.MainActivity
import com.bagha.echomemory.MainActivity.Companion.DB
import com.bagha.echomemory.MainActivity.Companion.memoryInfoList
import com.bagha.echomemory.R
import com.bagha.echomemory.databinding.FragmentTransformBinding
import java.util.Calendar

class ListOFMemoryFragment : Fragment() {

    private var _binding: FragmentTransformBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageOption : B_SelectImage
    private lateinit var adapterListMemory : Adapter_MemoryTitleList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val transformViewModel = ViewModelProvider(this).get(TransformViewModel::class.java)
        _binding = FragmentTransformBinding.inflate(inflater, container, false)
        val root: View = binding.root

        try {

            onClickBtn()
            imageOption = B_SelectImage(requireActivity())
            GifImageCall(requireContext()).ShowYOYOCustom(binding.fab)
            //___List____
            adapterListMemory = Adapter_MemoryTitleList(
                requireActivity() , MainActivity.memoryInfoList!! ,
                object : Adapter_MemoryTitleList.ActionOnClick{
                    override fun playMemory(position: Int) {
                        MainActivity.memoryTitleId = memoryInfoList[position].id
                        findNavController().navigate(R.id.nav_slideshow)
                    }

                }
            )
            //binding.recyclerviewTransform.setLayoutManager(LinearLayoutManager(requireContext()))
            binding.recyclerviewTransform.adapter = adapterListMemory



        }
        catch (e : IllegalStateException){
            e.printStackTrace()
        }
        catch (e : DeadObjectException){
            e.printStackTrace()
        }
        catch (e : NullPointerException){
            e.printStackTrace()
        }
        catch (e : Exception){
            e.printStackTrace()
        }


        return root
    }

    private fun onClickBtn() {
        //***********AddNewTitle_fab
        binding.fab?.setOnClickListener { view ->
            //GlobalFunction().ShowSnakBar(view , "Replace with your own action" )
            if(binding.addNewMemoryTitle!!.CardViewAddNewMemo!!.visibility == View.GONE){
                binding.addNewMemoryTitle!!.CardViewAddNewMemo!!.visibility = View.VISIBLE
            }else{
                binding.addNewMemoryTitle!!.CardViewAddNewMemo!!.visibility = View.GONE
            }
            resetAddNewMemoir()
        }

        //***********addNewTitle
        binding.addNewMemoryTitle!!.buttonAddNewMemoBtn!!.setOnClickListener(View.OnClickListener {
            try {
                val title = binding.addNewMemoryTitle!!.editTextAddTitleMemo!!.text.toString().trim()
                if(title.trim().isNullOrEmpty()){
                    //GlobalFunction().ShowSnakBar(binding.coordinatorLayout!! , getString(R.string.enterTitle))
                    GlobalFunction().ShowToast(requireContext() , getString(R.string.enterTitle))
                    return@OnClickListener
                }
                val dec = binding.addNewMemoryTitle!!.editTextAddDescription!!.text.toString().trim()
                val image = mCurrentPhotoPath
                val calendar = Calendar.getInstance()
                val date = calendar.get(Calendar.YEAR).toString()+
                        (calendar.get(Calendar.MONTH)+1).toString()+
                        calendar.get(Calendar.DAY_OF_MONTH).toString()+"_"+
                        calendar.get(Calendar.HOUR_OF_DAY).toString()+
                        calendar.get(Calendar.MINUTE).toString()+
                        calendar.get(Calendar.SECOND).toString()
                val memoInfoModel: MemoInfo_Table = MemoInfo_Table( 0 , title , date , image , dec)
                DB.insert_memoInfo(memoInfoModel)
                //GlobalFunction().ShowSnakBar(binding.coordinatorLayout!! , getString(R.string.successAddNewMemoir))
                GlobalFunction().ShowToast(requireContext() , getString(R.string.successAddNewMemoir))

                resetAddNewMemoir()
                binding.addNewMemoryTitle!!.CardViewAddNewMemo!!.visibility = View.GONE

                if(adapterListMemory != null){
                    memoryInfoList = DB.getAllMemoList()
                    adapterListMemory.notifyDataSetChanged()
                    binding.recyclerviewTransform.adapter = adapterListMemory
                }
            }
            catch (e : NullPointerException){
                e.printStackTrace()

            }
            catch (e : Exception){
                e.printStackTrace()

            }

        })


        //***********addNewTitle_CheckBax
        binding.addNewMemoryTitle!!.checkBoxIsHaveImageOrDescription!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.addNewMemoryTitle!!.linearLayoutAddImageOrDescription!!.visibility = View.VISIBLE
                binding.addNewMemoryTitle!!.linearLayoutAddImageOrDescription!!.visibility = View.VISIBLE
            } else {
                binding.addNewMemoryTitle!!.linearLayoutAddImageOrDescription!!.visibility = View.GONE
            }
        }

        //***********addNewTitle_SelectImage
        binding.addNewMemoryTitle!!.buttonSelectImageBtn!!.setOnClickListener(View.OnClickListener {
            if(B_CheckPermission().checkPermissions_Media(requireActivity())){
                B_SelectImage(requireActivity()).SelecetImageMassage_2(
                    requireActivity(),
                    null,
                    onActivityResultListOfMemory
                )

            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




    private fun resetAddNewMemoir() {
        binding.addNewMemoryTitle!!.editTextAddTitleMemo!!.text.clear()
        binding.addNewMemoryTitle!!.editTextAddDescription!!.text.clear()
        binding.addNewMemoryTitle!!.checkBoxIsHaveImageOrDescription!!.isChecked = false
        binding.addNewMemoryTitle!!.AppCompatImageViewCaverMemoir!!.setImageResource(0)
        mCurrentPhotoPath=""
    }


    //______________ Gallery_Action
    private val onActivityResultListOfMemory : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            (imageOption!!.onActivityResult_2(
                    result.data,
                    binding.addNewMemoryTitle.AppCompatImageViewCaverMemoir

                )
            )

        }
    }
}//end fragment