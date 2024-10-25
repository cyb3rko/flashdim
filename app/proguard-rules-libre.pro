# For stack traces
-keepattributes SourceFile, LineNumberTable

# Get rid of package names, makes file smaller
-repackageclasses

# Keep all FlashDim class names, except accessibility service and accessibility info dialog
-keep class !com.cyb3rko.flashdim.service.VolumeButtonService,!com.cyb3rko.flashdim.modals.AccessibilityInfoDialog, com.cyb3rko.flashdim.**
