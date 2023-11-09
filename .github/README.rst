Ghetto-Android
##############

Overview
--------

This repository shows the source code of the Python-Android package `[GhettoRecorder] <https://github.com/44xtc44/GhettoRecorder>`_.

The app uses Java to Python calls via Chaquopy plugin for Gradle. https://github.com/chaquo/chaquopy

All SVG images are created with Inkscape.

|screenshot|  |studio|

.. |screenshot| image:: ./screenshot_mobile.png
   :alt: screenshot_mobile
   :class: with-border
   :height: 600

.. |studio| image:: ./android_studio_main.png
   :alt: android_studio_main
   :class: with-border
   :width: 600


|

Running the project
-------------------

Install the latest Android Studio.

Start a virtual device (AVD) and enable the preinstalled Browser with JavaScript enabled.

Option: Enable Notifications in the "Settings" app or menu.

How it works
------------

* `[MainActivity.java] <https://github.com/44xtc44/Ghetto-Android/blob/dev/app/src/main/java/com/rhorn/ui/MainActivity.java>`_ Start/Stop the ForeGroundService running GhettoRecorder. TextView informs user.

* `[PermissionRequest.java] <https://github.com/44xtc44/Ghetto-Android/blob/dev/app/src/main/java/com/rhorn/ui/PermissionRequest.java>`_ Deal with the multi-access Permissions handling, for storage.

* `[runGhettoRecorder.java] <https://github.com/44xtc44/Ghetto-Android/blob/dev/app/src/main/java/com/rhorn/Ghetto/runGhettoRecorder.java>`_ ForeGroundService has to implement a notification routine.


License
-------

MIT