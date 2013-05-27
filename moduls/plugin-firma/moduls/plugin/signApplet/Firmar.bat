SET PATH= PATH$;"C:\java\32b\jdk1.5.0_22\bin"
cd C:\rafa\Indra\EclipseWorkspaceSistraSF-new\plugins-caib\moduls\plugin-firma
del moduls\plugin\signApplet\appletFirma.jar

REM jarsigner -keystore moduls\plugin\signApplet\ejemplo.ks -storepass ejemplo -keypass ejemplo -signedjar moduls\plugin\signApplet\appletFirma.jar output\product\appletFirma.jar ejemplo

jarsigner -keystore moduls\plugin\signApplet\ejemplo.ks -storepass ejemplo -keypass ejemplo -signedjar moduls\plugin\signApplet\AppletFirma.jar moduls\plugin\signApplet\AppletFirma-unsigned.jar ejemplo


pause