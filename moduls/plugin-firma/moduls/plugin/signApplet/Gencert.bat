SET PATH= PATH$;"C:\Archivos de programa\Java\jdk1.5.0_11\bin"
cd C:\Rafa\Indra\EclipseWorkspaceLiberarSistra\plugin-firmaCAIB\moduls\plugins\etc\
del ejemplo.ks
keytool -genkey -alias ejemplo -validity 3600 -v -keystore ejemplo.ks -storepass ejemplo