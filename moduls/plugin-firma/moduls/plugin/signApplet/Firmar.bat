SET PATH= PATH$;"C:\Archivos de programa\Java\jdk1.5.0_11\bin"
cd C:\Rafa\Indra\EclipseWorkspaceLiberarSistra\plugin-firmaCAIB
del moduls\plugins\signApplet\appletFirma.jar
jarsigner -keystore moduls\plugins\signApplet\ejemplo.ks -storepass ejemplo -keypass ejemplo -signedjar moduls\plugins\signApplet\appletFirma.jar output\product\appletFirma.jar ejemplo

pause