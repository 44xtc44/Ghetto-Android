from ghettorecorder import __main__ as ghetto_http_server


def ghetto_main(arg_for_cha=None):
    """ chakko wants an argument.

    We have to copy settings.ini to user folder.
    JAVA/Kotlin must ask for write permission first.
    """
    ghetto_http_server.main()

if __name__ == "__main__":
    ghetto_main()
