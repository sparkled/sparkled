function schedulerCtrl(moment,
                       musicPlayerRestService,
                       schedulerRestService,
                       songRestService,
                       toastr,
                       $scope,
                       _) {
    'ngInject';

    const vm = this;
    const dateFormat = 'DD/MM/YYYY';

    vm.date = moment();
    vm.scheduledSongs = [];
    vm.songs = [];

    vm.songToSchedule = null;
    vm.scheduleTime = moment().format('HH:mm:ss');
    vm.scheduleEndTime = null;
    vm.canSchedule = false;

    vm.stopCurrentSong = stopCurrentSong;
    vm.schedule = schedule;
    vm.unschedule = unschedule;

    vm.datePickerOptions = {
        singleDatePicker: true,
        timePicker: false,
        locale: {
            format: dateFormat
        }
    };

    $scope.$watch(() => vm.songToSchedule, updateCanSchedule);
    $scope.$watch(() => vm.scheduleTime, updateCanSchedule);

    function updateCanSchedule() {
        vm.canSchedule = _(vm.songToSchedule).get('id') != null && isValidTime(vm.scheduleTime);

        vm.scheduleEndTime = null;
        if (vm.canSchedule) {
            const song = findSongById(vm.songToSchedule.id);
            const durationSeconds = Math.ceil(song.durationFrames / song.framesPerSecond);
            vm.scheduleEndTime = buildStartTime().add(durationSeconds, 'seconds').toDate();
        }
    }

    function isValidTime(time) {
        const timeObject = getSplitTime(time);
        var validHour = timeObject.hour >= 0 && timeObject.hour < 24;
        var validMinute = timeObject.minute >= 0 && timeObject.minute < 60;
        var validSecond = timeObject.second >= 0 && timeObject.second < 60;

        return validHour && validMinute && validSecond;
    }


    loadSongs().then(() => {
        $scope.$watch(() => vm.date, loadScheduledSongs);
    });

    function loadSongs() {
        return songRestService.getSongs().then(songs => {
            vm.songs = songs;
        });
    }

    function stopCurrentSong() {
        musicPlayerRestService.stopCurrentSong()
            .then(loadScheduledSongs);
    }

    function schedule() {
        const scheduledSong = {
            songId: vm.songToSchedule.id,
            startTime: buildStartTime()
        };

        schedulerRestService.scheduleSong(scheduledSong)
            .then(onScheduleSuccess, onScheduleFailure);
    }

    function buildStartTime() {
        return moment(vm.date).set(getSplitTime(vm.scheduleTime));
    }

    function getSplitTime(time) {
        const timeParts = time ? time.split(':') : [];
        return {
            hour: parseInt(timeParts[0]),
            minute: parseInt(timeParts[1]),
            second: parseInt(timeParts[2])
        };
    }

    function unschedule(scheduledSong) {
        schedulerRestService.unscheduleSong(scheduledSong).then(loadScheduledSongs);
    }

    function onScheduleSuccess() {
        loadScheduledSongs();
    }

    function onScheduleFailure(rejection) {
        toastr.error(rejection.data);
    }

    function loadScheduledSongs() {
        if (vm.date) {
            schedulerRestService.getScheduledSongs(vm.date.format()).then(response => {
                vm.scheduledSongs = response;
                _(vm.scheduledSongs).each(scheduledSong => {
                    scheduledSong.song = findSongById(scheduledSong.songId);
                });
            });
        }
    }

    function findSongById(songId) {
        return _.find(vm.songs, song => song.id === songId)
    }
}

module.exports = {
    name: 'schedulerCtrl',
    fn: schedulerCtrl
};
