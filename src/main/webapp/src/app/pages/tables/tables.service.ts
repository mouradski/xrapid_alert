import {Observable, Subject} from "rxjs";

export class TablesService {
    private data = new Subject<any>();

    getData(): Observable<any> {
        return this.data.asObservable();
    }


    updateData(data: any) {
        this.data.next(data);
    }


}
