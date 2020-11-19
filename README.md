# Battlefield
### A small confrontation simulator
#### Dependencies: [JOGL](https://jogamp.org/)
* Download library [rc](https://jogamp.org/deployment/archive/rc/)/<*version*>/archive/jogamp-all-platforms.7z
* Unpack
* Use jar/gluegen-rt.jar and jar/jogl-all.jar as dependencies
* Use jar/gluegen-rt-natives-<*your platform*>.jar and jar/jogl-all-natives-<*your platform*>.jar **or**  
    lib/<*your platform*>/libgluegen-rt.<*ext*>,  
    lib/<*your platform*>/libjogl_desktop.<*ext*>,  
    lib/<*your platform*>/libnativewindow_awt.<*ext*> and  
    lib/<*your platform*>/libnativewindow_<*your window system*>.<*ext*>  
    as dependencies

Note: tested in Linux Mint 20 only  
Note: to avoid issues with JogAmp recommend using jdk-8 and JogAmp version `v2.3.1`
