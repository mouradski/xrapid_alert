import {Observable, Subject} from "rxjs";

export class TablesService {
    private data = new Subject<any>();
    private singleData = new Subject<any>();
    getData(): Observable<any> {
        return this.data.asObservable();
    }

    getSingleData(): Observable<any> {
        return this.singleData.asObservable();
    }


    updateData(data: any) {
        this.data.next(data);
    }

    single(data: any) {
        this.singleData.next(data);
    }


}
