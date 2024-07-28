import React, { useEffect, useState } from 'react';
import { Modal as M, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@nextui-org/react";
import { useShopservice } from '@/services/useShopService';

interface ModalProps {
  isOpen: boolean;
  onOpen: () => void;
  onOpenChange: () => void;
  status: number;
  setStatus: (status: number) => void;
}

function Modal({ isOpen, onOpen, onOpenChange, status, setStatus }: ModalProps) {
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [currentStatus, setCurrentStatus] = useState<number>(status);
  const [errorMessage, setErrorMessage] = useState<string>('');
  const shopService = useShopservice();

  const submit = async (onClose: () => void) => {
    setIsLoading(true);

    try {
      const resp = await shopService.updateStatus({status: currentStatus});
      if (resp.code === 1) {
        setStatus(currentStatus);
        setIsLoading(false);
        onClose();
      } else {
        setIsLoading(false);
        setErrorMessage("Failed to update status");
      }
    } catch (e) {
      setIsLoading(false);
      setErrorMessage("Failed to update status");
    }
  };

  useEffect(() => {
    setCurrentStatus(status);
  }, [status]);

  return (
    <div className='flex items-center justify-center'>
      <M isOpen={isOpen} onOpenChange={onOpenChange} backdrop="blur">
        <ModalContent>
          {(onClose) => (
            <div>
              <ModalHeader className="flex flex-col gap-1 pb-0">Set Opening Status</ModalHeader>

              <ModalBody>
                {errorMessage && <p className="text-red-500 absolute left-1/2 -translate-x-1/2">{errorMessage}</p>}
                <div className="flex gap-4 mt-8 mb-6 justify-around">
                  <label>
                    <input
                      type="radio"
                      value="1"
                      checked={currentStatus === 1}
                      onClick={() => setErrorMessage('')}
                      onChange={(e) => { setCurrentStatus(+e.target.value); setErrorMessage('') }}
                    />
                    Opening
                  </label>
                  <label>
                    <input
                      type="radio"
                      value="0"
                      onClick={() => setErrorMessage('')}
                      checked={currentStatus === 0}
                      onChange={(e) => { setCurrentStatus(+e.target.value); setErrorMessage('') }}
                    />
                    Close
                  </label>
                </div>
              </ModalBody>

              <ModalFooter className='pt-0'>
                <button onClick={onClose} className="px-5 py-2 rounded-md bg-gray-200 hover:bg-gray-300 focus:border-0 mr-4">
                  Cancel
                </button>
                <button onClick={() => { submit(onClose) }} className="px-5 py-2 rounded-md bg-sky-400 hover:bg-sky-500 focus:border-0">
                  {isLoading ? 'Loading...' : 'Update'}
                </button>
              </ModalFooter>
            </div>
          )}
        </ModalContent>
      </M>
    </div>
  );
}

export default Modal;
