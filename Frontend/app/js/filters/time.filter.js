function timeFilter() {
    return (input) => {
        if (input == null || isNaN(input)) {
            return '';
        }

        const isNegative = input < 0;
        if (isNegative) {
            input = -input;
        }

        const hours = pad(Math.floor(input / 3600));
        const minutes = pad(Math.floor((input % 3600) / 60));
        const seconds = pad(Math.floor(input % 60));

        const sign = isNegative ? '-' : '';
        const hasHours = hours > 0;

        if (hasHours) {
            return `${sign}${hours}:${minutes}:${seconds}`;
        } else {
            return `${sign}${minutes}:${seconds}`;
        }
    };

    function pad(input) {
        return input < 10 ? '0' + input : input;
    }
}

module.exports = {
    name: 'time',
    fn: timeFilter
};
