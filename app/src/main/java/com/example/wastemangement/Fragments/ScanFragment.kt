package com.example.wastemangement.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemangement.Activities.WasteClassificationActivity
import com.example.wastemangement.Adapters.DetectedItemAdapter
import com.example.wastemangement.ObjectDetectorHelper
import com.example.wastemangement.OverlayView
import com.example.wastemangement.R
import com.example.wastemangement.databinding.InfoBottomSheetBinding
import org.tensorflow.lite.task.gms.vision.detector.Detection
import java.util.LinkedList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanFragment : Fragment(), ObjectDetectorHelper.DetectorListener {
    private val TAG = "CameraFragment"
    private lateinit var objectDetectorHelper: ObjectDetectorHelper
    private lateinit var bitmapBuffer: Bitmap
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private lateinit var viewfinder:PreviewView
    private lateinit var progressBar:ContentLoadingProgressBar
    private lateinit var thresholdPlus:AppCompatImageButton
    private lateinit var thresholdMinus:AppCompatImageButton
    private lateinit var maxResultsPlus:AppCompatImageButton
    private lateinit var maxResultsMinus:AppCompatImageButton
    private lateinit var threadsPlus:AppCompatImageButton
    private lateinit var threadsMinus:AppCompatImageButton
    private lateinit var spinnerDelegate:AppCompatSpinner
    private lateinit var spinnerModel:AppCompatSpinner
    private lateinit var thresholdValue:TextView
    private lateinit var threadValue:TextView
    private lateinit var maxResultValue:TextView
    private lateinit var overlayView: OverlayView
    private lateinit var interferenceValue:TextView

    private lateinit var layout1: RelativeLayout
    private lateinit var layout2: RelativeLayout

    private var detectedList:ArrayList<String> = arrayListOf()
    private lateinit var displayList:RecyclerView
    private lateinit var listAdapter:DetectedItemAdapter
    private lateinit var submitButton: AppCompatButton


    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService

    override fun onResume() {
        super.onResume()
        // Make sure that all permissions are still present, since the
        // user could have removed them while the app was in paused state.
        /*if (!PermissionsFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(CameraFragmentDirections.actionCameraToPermissions())*/
        }

    override fun onDestroyView() {
        super.onDestroyView()

        // Shut down our background executor
        cameraExecutor.shutdown()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewfinder=view.findViewById(R.id.view_finder)
        progressBar=view.findViewById(R.id.progress_circular)
        thresholdPlus=view.findViewById(R.id.threshold_plus)
        thresholdMinus=view.findViewById(R.id.threshold_minus)
        maxResultsPlus=view.findViewById(R.id.max_results_plus)
        maxResultsMinus=view.findViewById(R.id.max_results_minus)
        threadsPlus=view.findViewById(R.id.threads_plus)
        threadsMinus=view.findViewById(R.id.threads_minus)
        spinnerDelegate=view.findViewById(R.id.spinner_delegate)
        spinnerModel=view.findViewById(R.id.spinner_model)
        thresholdValue=view.findViewById(R.id.threshold_value)
        threadValue=view.findViewById(R.id.threads_value)
        maxResultValue=view.findViewById(R.id.max_results_value)
        overlayView=view.findViewById(R.id.overlay)
        interferenceValue=view.findViewById(R.id.inference_time_val)
        submitButton=view.findViewById(R.id.submitButton)

        layout1=view.findViewById(R.id.unnecessary)
        layout2=view.findViewById(R.id.unnecessary2)

        displayList=view.findViewById(R.id.DisplayList)
        displayList.layoutManager=LinearLayoutManager(context)
        listAdapter = DetectedItemAdapter(requireContext(),detectedList)
        displayList.adapter=listAdapter
        objectDetectorHelper = ObjectDetectorHelper(
            context = requireContext(),
            objectDetectorListener = this)

        layout1.visibility=View.GONE
        layout2.visibility=View.GONE
        // Attach listeners to UI control widgets
        initBottomSheetControls()
    }

    private fun initBottomSheetControls() {
        // When clicked, lower detection score threshold floor
        thresholdMinus.setOnClickListener {
            if (objectDetectorHelper.threshold >= 0.1) {
                objectDetectorHelper.threshold -= 0.1f
                updateControlsUi()
            }
        }

        // When clicked, raise detection score threshold floor
        thresholdPlus.setOnClickListener {
            if (objectDetectorHelper.threshold <= 0.8) {
                objectDetectorHelper.threshold += 0.1f
                updateControlsUi()
            }
        }

        // When clicked, reduce the number of objects that can be detected at a time
        maxResultsMinus.setOnClickListener {
            if (objectDetectorHelper.maxResults > 1) {
                objectDetectorHelper.maxResults--
                updateControlsUi()
            }
        }

        // When clicked, increase the number of objects that can be detected at a time
        maxResultsPlus.setOnClickListener {
            if (objectDetectorHelper.maxResults < 5) {
                objectDetectorHelper.maxResults++
                updateControlsUi()
            }
        }

        // When clicked, decrease the number of threads used for detection
        threadsMinus.setOnClickListener {
            if (objectDetectorHelper.numThreads > 1) {
                objectDetectorHelper.numThreads--
                updateControlsUi()
            }
        }

        // When clicked, increase the number of threads used for detection
        threadsPlus.setOnClickListener {
            if (objectDetectorHelper.numThreads < 4) {
                objectDetectorHelper.numThreads++
                updateControlsUi()
            }
        }

        // When clicked, change the underlying hardware used for inference. Current options are CPU
        // GPU, and NNAPI
        spinnerDelegate.setSelection(0, false)
        spinnerDelegate.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    objectDetectorHelper.currentDelegate = p2
                    updateControlsUi()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    /* no op */
                }
            }
        submitButton.setOnClickListener {

            val intent = Intent(activity,WasteClassificationActivity::class.java)
            intent.putStringArrayListExtra("LabelList",detectedList)
            startActivity(intent)
        }
        // When clicked, change the underlying model used for object detection
        spinnerModel.setSelection(0, false)
        spinnerModel.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    objectDetectorHelper.currentModel = p2
                    updateControlsUi()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    /* no op */
                }
            }
    }

    // Update the values displayed in the bottom sheet. Reset detector.
    private fun updateControlsUi() {
        maxResultValue.text =
            objectDetectorHelper.maxResults.toString()
        thresholdValue.text =
            String.format("%.2f", objectDetectorHelper.threshold)
        threadValue.text =
            objectDetectorHelper.numThreads.toString()

        // Needs to be cleared instead of reinitialized because the GPU
        // delegate needs to be initialized on the thread using it when applicable
        objectDetectorHelper.clearObjectDetector()
        overlayView.clear()
    }

    // Initialize CameraX, and prepare to bind the camera use cases
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                // CameraProvider
                cameraProvider = cameraProviderFuture.get()

                // Build and bind the camera use cases
                bindCameraUseCases()
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    // Declare and bind preview, capture and analysis use cases
    @SuppressLint("UnsafeOptInUsageError")
    private fun bindCameraUseCases() {

        // CameraProvider
        val cameraProvider =
            cameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector - makes assumption that we're only using the back camera
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        // Preview. Only using the 4:3 ratio because this is the closest to our models
        preview =
            Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(viewfinder.display.rotation)
                .build()

        // ImageAnalysis. Using RGBA 8888 to match how our models work
        imageAnalyzer =
            ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(viewfinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(cameraExecutor) { image ->
                        if (!::bitmapBuffer.isInitialized) {
                            // The image rotation and RGB image buffer are initialized only once
                            // the analyzer has started running
                            bitmapBuffer = Bitmap.createBitmap(
                                image.width,
                                image.height,
                                Bitmap.Config.ARGB_8888
                            )
                        }

                        detectObjects(image)
                    }
                }

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(viewfinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun detectObjects(image: ImageProxy) {
        // Copy out RGB bits to the shared bitmap buffer
        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }

        val imageRotation = image.imageInfo.rotationDegrees
        // Pass Bitmap and rotation to the object detector helper for processing and detection
        objectDetectorHelper.detect(bitmapBuffer, imageRotation)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation = viewfinder.display.rotation
    }

    // Update UI after objects have been detected. Extracts original image height/width
    // to scale and place bounding boxes properly through OverlayView
    override fun onResults(
        results: MutableList<Detection>?,
        inferenceTime: Long,
        imageHeight: Int,
        imageWidth: Int
    ) {

        addToList(results!!)
        activity?.runOnUiThread {
            interferenceValue.text =
                String.format("%d ms", inferenceTime)

            // Pass necessary information to OverlayView for drawing on the canvas
            overlayView.setResults(
                results ?: LinkedList<Detection>(),
                imageHeight,
                imageWidth
            )

            // Force a redraw
            overlayView.invalidate()
            listAdapter.updateList(detectedList)
        }
    }

    private fun addToList(results: MutableList<Detection>) {
        for(result in results)
        {
            val label = result.categories[0].label
            if(!detectedList.contains(label)) {
                if(result.categories[0].score>0.6) {
                    detectedList.add(label)
                    Log.i("test", label)

                }
            }
        }
    }

    override fun onError(error: String) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onInitialized() {
        objectDetectorHelper.setupObjectDetector()
        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Wait for the views to be properly laid out
        viewfinder.post {
            // Set up the camera and its use cases
            setUpCamera()
        }
        progressBar.visibility = View.GONE
    }
}