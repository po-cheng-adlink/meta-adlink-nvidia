#
# From NVidia's tegra-minimal-initramfs.bb
#
## XXX
## Temporarily override this function from sstate.bbclass
## until a better solution is found.
## XXX
#python sstate_report_unihash() {
#    report_unihash = getattr(bb.parse.siggen, 'report_unihash', None)
#
#    if report_unihash:
#        ss = sstate_state_fromvars(d)
#        if ss['task'] == 'image_complete':
#            os.environ['PSEUDO_DISABLED'] = '1'
#        report_unihash(os.getcwd(), ss['task'], d)
#}

#
# the overriding sstate_report_unihash reports a bug where do_package ubuntu's
# rootfs extracted from docker container cause exception as follows:
#
# Exception: Exception: KeyError: 'getgrgid(): gid not found: 42'
# Path ./package/etc/gshadow is owned by uid 0, gid 42, which doesn't match any user/group on target. This may be due to host contamination.
#
# from sstate.bbclass the origin sstate_report_unihash()
#
#python sstate_report_unihash() {
#   report_unihash = getattr(bb.parse.siggen, 'report_unihash', None)
#
#   if report_unihash:
#       ss = sstate_state_fromvars(d)
#       report_unihash(os.getcwd(), ss['task'], d)
#}
#
# report_unihash() eventually calls to def OEOuthashBasic(path, sigfile, task, d): in sstatesig.py where getgrpid() is called
#
#def OEOuthashBasic(path, sigfile, task, d):
#    """
#    Basic output hash function
#
#    Calculates the output hash of a task by hashing all output file metadata,
#    and file contents.
#    """
#    import hashlib
#    import stat
#    import pwd
#    import grp
#
#    def update_hash(s):
#        s = s.encode('utf-8')
#        h.update(s)
#        if sigfile:
#            sigfile.write(s)
#
#    h = hashlib.sha256()
#    prev_dir = os.getcwd()
#    include_owners = os.environ.get('PSEUDO_DISABLED') == '0'
#    if "package_write_" in task or task == "package_qa":
#        include_owners = False
#    include_timestamps = False
#    if task == "package":
#        include_timestamps = d.getVar('BUILD_REPRODUCIBLE_BINARIES') == '1'
#    extra_content = d.getVar('HASHEQUIV_HASH_VERSION')
#
#    try:
#        os.chdir(path)
#
#        update_hash("OEOuthashBasic\n")
#        if extra_content:
#            update_hash(extra_content + "\n")
#
#        # It is only currently useful to get equivalent hashes for things that
#        # can be restored from sstate. Since the sstate object is named using
#        # SSTATE_PKGSPEC and the task name, those should be included in the
#        # output hash calculation.
#        update_hash("SSTATE_PKGSPEC=%s\n" % d.getVar('SSTATE_PKGSPEC'))
#        update_hash("task=%s\n" % task)
#
#        for root, dirs, files in os.walk('.', topdown=True):
#            # Sort directories to ensure consistent ordering when recursing
#            dirs.sort()
#            files.sort()
#
#            def process(path):
#                s = os.lstat(path)
#
#                if stat.S_ISDIR(s.st_mode):
#                    update_hash('d')
#                elif stat.S_ISCHR(s.st_mode):
#                    update_hash('c')
#                elif stat.S_ISBLK(s.st_mode):
#                    update_hash('b')
#                elif stat.S_ISSOCK(s.st_mode):
#                    update_hash('s')
#                elif stat.S_ISLNK(s.st_mode):
#                    update_hash('l')
#                elif stat.S_ISFIFO(s.st_mode):
#                    update_hash('p')
#                else:
#                    update_hash('-')
#
#                def add_perm(mask, on, off='-'):
#                    if mask & s.st_mode:
#                        update_hash(on)
#                    else:
#                        update_hash(off)
#
#                add_perm(stat.S_IRUSR, 'r')
#                add_perm(stat.S_IWUSR, 'w')
#                if stat.S_ISUID & s.st_mode:
#                    add_perm(stat.S_IXUSR, 's', 'S')
#                else:
#                    add_perm(stat.S_IXUSR, 'x')
#
#                add_perm(stat.S_IRGRP, 'r')
#                add_perm(stat.S_IWGRP, 'w')
#                if stat.S_ISGID & s.st_mode:
#                    add_perm(stat.S_IXGRP, 's', 'S')
#                else:
#                    add_perm(stat.S_IXGRP, 'x')
#
#                add_perm(stat.S_IROTH, 'r')
#                add_perm(stat.S_IWOTH, 'w')
#                if stat.S_ISVTX & s.st_mode:
#                    update_hash('t')
#                else:
#                    add_perm(stat.S_IXOTH, 'x')
#
#                if include_owners:
#                    try:
#                        update_hash(" %10s" % pwd.getpwuid(s.st_uid).pw_name)
#                        update_hash(" %10s" % grp.getgrgid(s.st_gid).gr_name)
#                    except KeyError as e:
#                        bb.warn("KeyError in %s" % path)
#                        msg = ("KeyError: %s\nPath %s is owned by uid %d, gid %d, which doesn't match "
#                            "any user/group on target. This may be due to host contamination." % (e, path, s.st_uid, s.st_gid))
#                        raise Exception(msg).with_traceback(e.__traceback__)
#
#                if include_timestamps:
#                    update_hash(" %10d" % s.st_mtime)
#
#                update_hash(" ")
#                if stat.S_ISBLK(s.st_mode) or stat.S_ISCHR(s.st_mode):
#                    update_hash("%9s" % ("%d.%d" % (os.major(s.st_rdev), os.minor(s.st_rdev))))
#                else:
#                    update_hash(" " * 9)
#
#                update_hash(" ")
#                if stat.S_ISREG(s.st_mode):
#                    update_hash("%10d" % s.st_size)
#                else:
#                    update_hash(" " * 10)
#
#                update_hash(" ")
#                fh = hashlib.sha256()
#                if stat.S_ISREG(s.st_mode):
#                    # Hash file contents
#                    with open(path, 'rb') as d:
#                        for chunk in iter(lambda: d.read(4096), b""):
#                            fh.update(chunk)
#                    update_hash(fh.hexdigest())
#                else:
#                    update_hash(" " * len(fh.hexdigest()))
#
#                update_hash(" %s" % path)
#
#                if stat.S_ISLNK(s.st_mode):
#                    update_hash(" -> %s" % os.readlink(path))
#
#                update_hash("\n")
#
#            # Process this directory and all its child files
#            process(root)
#            for f in files:
#                if f == 'fixmepath':
#                    continue
#                process(os.path.join(root, f))
#    finally:
#        os.chdir(prev_dir)
#
#    return h.hexdigest()
#

#
# NVidia's Hack was to skip the grp.getgrgid by setting os.environ.get('PSEUDO_DISABLED') == '1'
# because here include_owners = include_owners = os.environ.get('PSEUDO_DISABLED') == '0'
#
# So our hack is as follows
#

python sstate_report_unihash() {
    report_unihash = getattr(bb.parse.siggen, 'report_unihash', None)

    if report_unihash:
        ss = sstate_state_fromvars(d)
        if ss['task'] == 'package':
            os.environ['PSEUDO_DISABLED'] = '1'
        report_unihash(os.getcwd(), ss['task'], d)
}

