import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'dateAgo',
    pure: true
})
export class DateAgoPipe implements PipeTransform {

    transform(value: any, args?: any): any {

        var minutes = Math.floor(value / 60);

        var seconds = value - minutes * 60;

        if (minutes == 0) {
            if (seconds < 3) {
                return 'Just Now';
            } else {
                return seconds + " sec ago";
            }
        } else {

            if (minutes < 2) {
                if (seconds < 2) {
                    return minutes + ' min and ' + seconds + ' sec ago';
                } else {
                    return minutes + ' min and ' + seconds + ' sec ago';
                }
            } else {
                if (seconds < 2) {
                    return minutes + ' min and ' + seconds + ' sec ago';
                } else {
                    return minutes + ' min and ' + seconds + ' sec ago';
                }
            }
        }
    }
}
