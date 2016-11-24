describe('timeFilter', function () {

    beforeEach(module('app.filters'));

    var timeFilter;
    beforeEach(
        inject(_timeFilter_ => timeFilter = _timeFilter_)
    );

    describe('timeFilter', timeFilterTests);

    function timeFilterTests() {
        it('Formats simple time', simpleTimeTest);
        it('Formats zero time', zeroTimeTest);
        it('Formats negative time', negativeTimeTest);
        it('Ignores non-numbers', ignoresNonNumbersTest);
        it('Ignores nulls', ignoresNullsTest);

        function simpleTimeTest() {
            var formattedTime = timeFilter(5000);
            expect(formattedTime).toBe('01:23:20');
        }

        function zeroTimeTest() {
            var formattedTime = timeFilter(0);
            expect(formattedTime).toBe('00:00');
        }

        function negativeTimeTest() {
            var formattedTime = timeFilter(-5000);
            expect(formattedTime).toBe('-01:23:20');
        }

        function ignoresNonNumbersTest() {
            var formattedTime = timeFilter('a');
            expect(formattedTime).toBe('');
        }

        function ignoresNullsTest() {
            var formattedTime = timeFilter(null);
            expect(formattedTime).toBe('');
        }
    }
});
